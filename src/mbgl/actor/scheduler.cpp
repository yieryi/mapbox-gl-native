#include <mbgl/actor/scheduler.hpp>
#include <mbgl/util/thread_pool.hpp>

namespace mbgl {

namespace {

thread_local Scheduler* g_currentScheduler;

}

void Scheduler::SetCurrent(Scheduler* scheduler) {
    g_currentScheduler = scheduler;
}

Scheduler* Scheduler::GetCurrent() {
    return g_currentScheduler;
}

// static
std::shared_ptr<Scheduler> Scheduler::GetBackground() {
    static std::weak_ptr<Scheduler> weak;
    static std::mutex mtx;

    std::lock_guard<std::mutex> lock(mtx);
    std::shared_ptr<Scheduler> scheduler = weak.lock();

    if (!scheduler) {
        weak = scheduler = std::make_shared<ThreadPool>(4);
    }

    return scheduler;
}

} //namespace mbgl
