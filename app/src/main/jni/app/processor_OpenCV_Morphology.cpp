//
// Created by SuperbiaYang on 2016/5/31.
//

#include "processor_OpenCV_Morphology.h"
#include <opencv2/opencv.hpp>
using namespace cv;

#define ELEMENT getStructuringElement(MORPH_RECT, Size(3, 3), Point(1, 1))

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_erode
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	erode(srcMat, dstMat, ELEMENT);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_dilate
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	dilate(srcMat, dstMat, ELEMENT);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_open
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	erode(srcMat, dstMat, ELEMENT);
	dilate(dstMat, dstMat, ELEMENT);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_close
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	dilate(srcMat, dstMat, ELEMENT);
    erode(dstMat, dstMat, ELEMENT);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}