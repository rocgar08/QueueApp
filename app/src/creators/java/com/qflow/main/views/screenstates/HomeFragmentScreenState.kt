package com.qflow.main.views.screenstates

import com.qflow.main.domain.local.models.Queue

sealed class HomeFragmentScreenState {
    class QueuesActiveObtained(val queues: List<Queue>) : HomeFragmentScreenState()
    class QueuesHistoricalObtained(val queues: List<Queue>) : HomeFragmentScreenState()
    class QueueCreatedCorrectly(val id: String) : HomeFragmentScreenState()
    class QueueInfoDialog(val queues: Queue): HomeFragmentScreenState()
    class QueueManageDialog(val queues: Queue) : HomeFragmentScreenState()
    class QueueClosed(val queues: Queue) : HomeFragmentScreenState()
}