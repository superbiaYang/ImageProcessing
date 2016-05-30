LOCAL_PATH := $(call my-dir)

include $(CLEAR_VARS)

OpenCV_INSTALL_MODULES:=on
include $(LOCAL_PATH)/../../../sdk/opencv/jni/OpenCV.mk

LOCAL_MODULE := OpenCVHelper
LOCAL_SRC_FILES := app/processor_OpenCV_Filter.cpp
include $(BUILD_SHARED_LIBRARY)