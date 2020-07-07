package com.bezgrebelny.imgurgalleryapp.app.common.delegateadapter

import android.view.LayoutInflater
import android.view.ViewGroup

class AdapterManager<U : UiModel>(private vararg val delegateItem: AdapterItem<*>) {

    private lateinit var layoutInflater: LayoutInflater

    private lateinit var currentAdapter: AdapterItem<U>

    // выбор адаптера
    @Suppress("UNCHECKED_CAST")
    fun getItemViewType(model: U): Int {
        if (delegateItem.isEmpty()) throw IllegalArgumentException("Список адаптеров пуст")
        delegateItem.forEachIndexed { index, delegateItem ->
            if (delegateItem.isModel(model)) {
                currentAdapter = delegateItem as AdapterItem<U>
                return index
            }
        }
        throw IllegalArgumentException("Для модели $model отсутствует адаптер")
    }

    fun onCreateViewHolder(parent: ViewGroup): ViewHolder<U> {
        if (!::layoutInflater.isInitialized) layoutInflater =
            LayoutInflater.from(parent.context)
        return ViewHolder(
            layoutInflater.inflate(
                currentAdapter.layoutRes,
                parent,
                false
            ),
            currentAdapter
        )
    }

    fun onBindViewHolder(holder: ViewHolder<U>, model: U) {
        currentAdapter.bindView(holder.itemView, model)
        holder.model = model
        holder.bindClick()
    }

    /** для узбежания утечки лучше очищать список адаптеров */
    fun cleanUp() {
        delegateItem.forEach {
            it.cleanUp()
        }
    }
}