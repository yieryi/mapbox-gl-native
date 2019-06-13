add_executable(mbgl-test-runner
    test-runner/main.cpp
    test-runner/parser.cpp
    test-runner/runner.cpp
)

if(APPLE)
    target_link_libraries(mbgl-test-runner PRIVATE mbgl-loop-darwin)
else()
    target_link_libraries(mbgl-test-runner PRIVATE mbgl-loop-uv)
endif()

target_include_directories(mbgl-test-runner
    PRIVATE src
    PRIVATE platform/default/include
    PRIVATE test-runner
)

target_link_libraries(mbgl-test-runner PRIVATE
    mbgl-core
    mbgl-filesource
    args
    cppcodec
    expected
    filesystem
    pixelmatch-cpp
    rapidjson
)

add_definitions(-DTEST_RUNNER_ROOT_PATH="${CMAKE_SOURCE_DIR}")
