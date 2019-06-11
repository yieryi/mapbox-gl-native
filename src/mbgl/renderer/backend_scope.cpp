#include <mbgl/gfx/backend_scope.hpp>
#include <mbgl/gfx/renderer_backend.hpp>

#include <cassert>

namespace {

thread_local mbgl::gfx::BackendScope* g_backendScope;

} // namespace

namespace mbgl {
namespace gfx {

BackendScope::BackendScope(RendererBackend& backend_, ScopeType scopeType_)
    : priorScope(g_backendScope),
      nextScope(nullptr),
      backend(backend_),
      scopeType(scopeType_) {
    if (priorScope) {
        assert(priorScope->nextScope == nullptr);
        priorScope->nextScope = this;
        priorScope->deactivate();
    }

    activate();

    g_backendScope = this;
}

BackendScope::~BackendScope() {
    assert(nextScope == nullptr);
    deactivate();

    if (priorScope) {
        priorScope->activate();
        g_backendScope = priorScope;
        assert(priorScope->nextScope == this);
        priorScope->nextScope = nullptr;
    } else {
        g_backendScope = nullptr;
    }
}

void BackendScope::activate() {
    if (scopeType == ScopeType::Explicit &&
            !(priorScope && this->backend == priorScope->backend) &&
            !(nextScope && this->backend == nextScope->backend)) {
        // Only activate when set to Explicit and
        // only once per RenderBackend
        backend.activate();
        activated = true;
    }
}

void BackendScope::deactivate() {
    if (activated &&
        !(nextScope && this->backend == nextScope->backend)) {
        // Only deactivate when set to Explicit and
        // only once per RenderBackend
        backend.deactivate();
        activated = false;
    }
}

bool BackendScope::exists() {
    return g_backendScope;
}

} // namespace gfx
} // namespace mbgl
