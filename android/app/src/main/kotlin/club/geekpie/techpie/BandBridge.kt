package club.geekpie.techpie

import android.content.Context
import android.util.Log
import com.xiaomi.xms.wearable.Wearable
import com.xiaomi.xms.wearable.auth.Permission
import com.xiaomi.xms.wearable.message.OnMessageReceivedListener
import com.xiaomi.xms.wearable.node.Node
import com.xiaomi.xms.wearable.service.OnServiceConnectionListener
import org.json.JSONObject

/**
 * 手环快应用（Vela system.interconnect）通信桥。
 * 协议见 vela_app/；当前为链路验证阶段：收到手环消息回 pong。
 */
class BandBridge(private val context: Context) {

    private val messageListener = OnMessageReceivedListener { nodeId, message ->
        val received = String(message, Charsets.UTF_8)
        Log.i(TAG, "message from $nodeId: $received")
        val reply = JSONObject().put("t", "pong").put("echo", received).toString()
        Wearable.getMessageApi(context).sendMessage(nodeId, reply.toByteArray())
            .addOnFailureListener { e -> Log.w(TAG, "sendMessage failed", e) }
    }

    fun start() {
        Wearable.getServiceApi(context).registerServiceConnectionListener(
            object : OnServiceConnectionListener {
                override fun onServiceConnected() {
                    Log.i(TAG, "wearable service connected")
                    attachNodes()
                }

                override fun onServiceDisconnected() {
                    Log.i(TAG, "wearable service disconnected")
                }
            },
        )
        attachNodes()
    }

    private fun attachNodes() {
        Wearable.getNodeApi(context).getConnectedNodes()
            .addOnSuccessListener { nodes -> nodes.forEach(::setupNode) }
            .addOnFailureListener { e -> Log.w(TAG, "getConnectedNodes failed", e) }
    }

    private fun setupNode(node: Node) {
        Log.i(TAG, "node ${node.id} (${node.name})")
        Wearable.getAuthApi(context)
            .requestPermission(node.id, Permission.DEVICE_MANAGER, Permission.NOTIFY)
            .addOnSuccessListener { listen(node.id) }
            .addOnFailureListener { e -> Log.w(TAG, "requestPermission failed", e) }
    }

    private fun listen(nodeId: String) {
        Wearable.getMessageApi(context).addListener(nodeId, messageListener)
            .addOnSuccessListener { Log.i(TAG, "listener attached for $nodeId") }
            .addOnFailureListener { e -> Log.w(TAG, "addListener failed", e) }
    }

    private companion object {
        private const val TAG = "BandBridge"
    }
}
