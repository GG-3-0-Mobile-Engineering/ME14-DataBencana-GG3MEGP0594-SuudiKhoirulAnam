package com.ferosburn.databencana.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ferosburn.databencana.R
import com.ferosburn.databencana.data.DisasterModel
import com.ferosburn.databencana.databinding.DisasterListItemBinding
import com.ferosburn.databencana.util.disasterValueToDisasterTypes
import com.ferosburn.databencana.util.provinceCodeToProvinces
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class DisasterListAdapter :
    ListAdapter<DisasterModel, DisasterListAdapter.DisasterListViewHolder>(DiffCallback) {
    class DisasterListViewHolder(
        private var binding: DisasterListItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(disasterItem: DisasterModel) {
            binding.apply {
                ivDisaster.load(disasterItem.imageUrl) {
                    placeholder(R.drawable.img_disaster_placeholder)
                    error(R.drawable.img_disaster_placeholder)
                }
                tvDisasterTime.text = LocalDateTime.parse(
                    disasterItem.createdAt,
                    DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                ).format(
                    DateTimeFormatter.ofPattern(
                        "cccc, d LLLL yyyy'\n'HH:mm:ss",
                        Locale.forLanguageTag("id")
                    )
                )
                tvDisasterType.text =
                    disasterItem.disasterType.disasterValueToDisasterTypes()?.disasterName
                tvProvince.text =
                    disasterItem.instanceRegionCode.provinceCodeToProvinces()?.provinceName
            }
        }
    }

    companion object DiffCallback : DiffUtil.ItemCallback<DisasterModel>() {
        override fun areItemsTheSame(oldItem: DisasterModel, newItem: DisasterModel): Boolean {
            return oldItem.pkey == newItem.pkey
        }

        override fun areContentsTheSame(oldItem: DisasterModel, newItem: DisasterModel): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DisasterListViewHolder {
        return DisasterListViewHolder(DisasterListItemBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: DisasterListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}