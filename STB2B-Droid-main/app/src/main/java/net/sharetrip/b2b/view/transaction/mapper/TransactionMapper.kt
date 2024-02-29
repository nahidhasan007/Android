package net.sharetrip.b2b.view.transaction.mapper

import net.sharetrip.b2b.view.transaction.model.Transaction
import net.sharetrip.b2b.view.transaction.model.TransactionListData
import net.sharetrip.b2b.view.transaction.view.transactionlist.TransactionUIItemData
import net.sharetrip.b2b.view.transaction.view.transactionlist.TransactionUIListData

class TransactionMapper {

    private fun mapFromModel(entity: Transaction): TransactionUIItemData {
        return TransactionUIItemData(
            amount = entity.amount,
            created_at = entity.created_at,
            isDebit = entity.isDebit,
            reference = entity.reference,
            status = entity.status,
            type = entity.type,
            uuid = entity.uuid,
            isDebitOrCredit = entity.isDebitOrCredit()
        )
    }

    fun mapListData(entity: TransactionListData): TransactionUIListData {
        return TransactionUIListData(
            data = entity.data?.let { entity.data.map { mapFromModel(it) } },
            offset = entity.offset,
            limit = entity.limit,
            count = entity.count
        )
    }
}