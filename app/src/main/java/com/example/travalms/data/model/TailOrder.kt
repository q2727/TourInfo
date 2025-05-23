data class TailOrder(
    val id: Int,
    val title: String,
    val content: List<String>,
    val publisherJid: String,
    val publishTime: Long,
    val productId: Int? = null,  // 关联产品ID
    val productTitle: String? = null  // 关联产品标题
) 