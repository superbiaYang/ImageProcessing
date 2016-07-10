//
// Created by SuperbiaYang on 2016/5/31.
//

#include "processor_OpenCV_Morphology.h"
#include <opencv2/opencv.hpp>
using namespace cv;

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_erode
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
	erode(srcMat, dstMat, element);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_dilate
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
	dilate(srcMat, dstMat, element);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_open
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
	erode(srcMat, dstMat, element);
	dilate(dstMat, dstMat, element);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Morphology_close
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
	dilate(srcMat, dstMat, element);
    erode(dstMat, dstMat, element);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}