package com.example.timesculptor.util

enum class SortEnum(var sort: Int) {
    TODAY(0), YESTERDAY(1), THIS_WEEK(2), THIS_MONTH(3), THIS_YEAR(4);

    companion object {
        fun getSortEnum(sort: Int): SortEnum {
            when (sort) {
                0 -> return TODAY
                1 -> return YESTERDAY
                2 -> return THIS_WEEK
                3 -> return THIS_MONTH
                4 -> return THIS_YEAR
            }
            return TODAY
        }
    }
}