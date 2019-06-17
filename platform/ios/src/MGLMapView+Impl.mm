#import "MGLMapView+Impl.h"
#import "MGLMapView+OpenGL.h"
#import "MGLStyle_Private.h"
#import "NSBundle+MGLAdditions.h"

#include <mbgl/gfx/backend.hpp>

NSString* const MGLMapboxRenderBackendDefaultsKey = @"MGLMapboxRenderBackend";

std::unique_ptr<MGLMapViewImpl> MGLMapViewImpl::Create(MGLMapView* nativeView) {
    static dispatch_once_t onceToken;
    dispatch_once (&onceToken, ^{
        NSString* backend = [NSProcessInfo processInfo].environment[@"MAPBOX_RENDER_BACKEND"];
        if (backend) {
            [[NSUserDefaults standardUserDefaults] setObject:backend
                                                      forKey:MGLMapboxRenderBackendDefaultsKey];
        } else {
            backend = [[NSUserDefaults standardUserDefaults]
                stringForKey:MGLMapboxRenderBackendDefaultsKey];
        }
        if (backend) {
            mbgl::gfx::Backend::SetType([backend UTF8String]);
        }
    });

    return mbgl::gfx::Backend::Create<MGLMapViewImpl, MGLMapView*>(nativeView);
}

MGLMapViewImpl::MGLMapViewImpl(MGLMapView* nativeView_) : mapView(nativeView_) {
}

void MGLMapViewImpl::render() {
    [mapView renderSync];
}

void MGLMapViewImpl::onCameraWillChange(mbgl::MapObserver::CameraChangeMode mode) {
    bool animated = mode == mbgl::MapObserver::CameraChangeMode::Animated;
    [mapView cameraWillChangeAnimated:animated];
}

void MGLMapViewImpl::onCameraIsChanging() {
    [mapView cameraIsChanging];
}

void MGLMapViewImpl::onCameraDidChange(mbgl::MapObserver::CameraChangeMode mode) {
    bool animated = mode == mbgl::MapObserver::CameraChangeMode::Animated;
    [mapView cameraDidChangeAnimated:animated];
}

void MGLMapViewImpl::onWillStartLoadingMap() {
    [mapView mapViewWillStartLoadingMap];
}

void MGLMapViewImpl::onDidFinishLoadingMap() {
    [mapView mapViewDidFinishLoadingMap];
}

void MGLMapViewImpl::onDidFailLoadingMap(mbgl::MapLoadError mapError, const std::string& what) {
    NSString *description;
    MGLErrorCode code;
    switch (mapError) {
        case mbgl::MapLoadError::StyleParseError:
            code = MGLErrorCodeParseStyleFailed;
            description = NSLocalizedStringWithDefaultValue(@"PARSE_STYLE_FAILED_DESC", nil, nil, @"The map failed to load because the style is corrupted.", @"User-friendly error description");
            break;
        case mbgl::MapLoadError::StyleLoadError:
            code = MGLErrorCodeLoadStyleFailed;
            description = NSLocalizedStringWithDefaultValue(@"LOAD_STYLE_FAILED_DESC", nil, nil, @"The map failed to load because the style can't be loaded.", @"User-friendly error description");
            break;
        case mbgl::MapLoadError::NotFoundError:
            code = MGLErrorCodeNotFound;
            description = NSLocalizedStringWithDefaultValue(@"STYLE_NOT_FOUND_DESC", nil, nil, @"The map failed to load because the style can’t be found or is incompatible.", @"User-friendly error description");
            break;
        default:
            code = MGLErrorCodeUnknown;
            description = NSLocalizedStringWithDefaultValue(@"LOAD_MAP_FAILED_DESC", nil, nil, @"The map failed to load because an unknown error occurred.", @"User-friendly error description");
    }
    NSDictionary *userInfo = @{
        NSLocalizedDescriptionKey: description,
        NSLocalizedFailureReasonErrorKey: @(what.c_str()),
    };
    NSError *error = [NSError errorWithDomain:MGLErrorDomain code:code userInfo:userInfo];
    [mapView mapViewDidFailLoadingMapWithError:error];
}

void MGLMapViewImpl::onWillStartRenderingFrame() {
    [mapView mapViewWillStartRenderingFrame];
}

void MGLMapViewImpl::onDidFinishRenderingFrame(mbgl::MapObserver::RenderMode mode) {
    bool fullyRendered = mode == mbgl::MapObserver::RenderMode::Full;
    [mapView mapViewDidFinishRenderingFrameFullyRendered:fullyRendered];
}

void MGLMapViewImpl::onWillStartRenderingMap() {
    [mapView mapViewWillStartRenderingMap];
}

void MGLMapViewImpl::onDidFinishRenderingMap(mbgl::MapObserver::RenderMode mode) {
    bool fullyRendered = mode == mbgl::MapObserver::RenderMode::Full;
    [mapView mapViewDidFinishRenderingMapFullyRendered:fullyRendered];
}

void MGLMapViewImpl::onDidFinishLoadingStyle() {
    [mapView mapViewDidFinishLoadingStyle];
}

void MGLMapViewImpl::onSourceChanged(mbgl::style::Source& source) {
    NSString *identifier = @(source.getID().c_str());
    MGLSource * nativeSource = [mapView.style sourceWithIdentifier:identifier];
    [mapView sourceDidChange:nativeSource];
}

void MGLMapViewImpl::onDidBecomeIdle() {
    [mapView mapViewDidBecomeIdle];
}

void MGLMapViewImpl::onStyleImageMissing(const std::string& imageIdentifier) {
    NSString *imageName = [NSString stringWithUTF8String:imageIdentifier.c_str()];
    [mapView didFailToLoadImage:imageName];
}
