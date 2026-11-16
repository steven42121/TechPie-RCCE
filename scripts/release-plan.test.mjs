import assert from 'node:assert/strict';
import { execFileSync, spawnSync } from 'node:child_process';
import { copyFileSync, mkdtempSync, mkdirSync, rmSync, writeFileSync } from 'node:fs';
import { tmpdir } from 'node:os';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';
import { test } from 'node:test';

// The CLI reads a real Git ledger. Every fixture/tag stays in its temporary repo.
function fixture(t, version, tags) {
  const root = mkdtempSync(join(tmpdir(), 'techpie-release-plan-'));
  t.after(() => rmSync(root, { recursive: true, force: true }));
  const scripts = join(root, 'scripts');
  mkdirSync(scripts);
  const source = dirname(fileURLToPath(import.meta.url));
  for (const file of ['release-plan.mjs', 'changelog.mjs']) copyFileSync(join(source, file), join(scripts, file));
  writeFileSync(join(root, 'pubspec.yaml'), `name: fixture\nversion: ${version}\n`);
  execFileSync('git', ['init', '--quiet'], { cwd: root });
  execFileSync('git', ['-c', 'user.name=Fixture', '-c', 'user.email=fixture@example.invalid', 'commit', '--allow-empty', '--quiet', '-m', 'fixture'], { cwd: root });
  for (const tag of tags) execFileSync('git', ['tag', tag], { cwd: root });
  return (...args) => spawnSync(process.execPath, [join(scripts, 'release-plan.mjs'), ...args], { cwd: root, encoding: 'utf8' });
}

test('a new RCCE stamp keeps its date-derived build number and recognizes the old release', (t) => {
  const run = fixture(t, '26.2.111614+26111614', ['v1.0.4+26', '26h2-111519']);
  assert.equal(run('--rcce-auto').status, 0);
  const report = run('--check', '--ref', 'master');
  assert.match(report.stdout, /newest_release=26\.2\.111519\+26111519/);
  assert.match(report.stdout, /suggested=26\.2\.111614\+26111614/);
  assert.equal(run('--next-version', '26.2.111614').stdout.trim(), '26.2.111614+26111614');
});

test('the current stamp can be rebuilt but an older master is rejected', (t) => {
  assert.equal(fixture(t, '26.2.111614+26111614', ['26h2-111614'])('--rcce-auto').status, 0);
  const run = fixture(t, '26.2.111519+26111519', ['26h2-111614']);
  assert.equal(run('--rcce-auto').status, 1);
  assert.equal(run('--require-version', '26.2.111519', '--ref', 'master').status, 1);
  assert.equal(run('--next-version', '26.2.111519').status, 1);
});

test('invalid dates and mismatching build numbers cannot publish', (t) => {
  for (const version of ['26.2.113014+26111614', '26.2.113114+26113114', '26.1.111614+26111614']) {
    assert.equal(fixture(t, version, [])('--rcce-auto').status, 1, version);
  }
});

test('a stale stamp at the end of a day suggests the next calendar hour', (t) => {
  const run = fixture(t, '26.2.111519+26111519', ['26h2-111523']);
  assert.match(run('--check', '--ref', 'master').stdout, /suggested=26\.2\.111600\+26111600/);
});

test('historical upstream iOS builds still count toward the build floor', (t) => {
  const run = fixture(t, '1.0.4+27', ['v1.0.3+17', 'ios-v1.0.4+26']);
  assert.equal(run('--next-version', '1.0.4').stdout.trim(), '1.0.4+27');
  assert.equal(run('--require-version', '1.0.2', '--ref', 'master').status, 1);
});
