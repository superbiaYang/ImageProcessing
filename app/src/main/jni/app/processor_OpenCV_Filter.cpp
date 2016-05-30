//
// Created by SuperbiaYang on 2016/5/30.
//
#include "processor_OpenCV_Filter.h"
#include <opencv2/opencv.hpp>

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_medianBlur
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint size)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	cv::Mat srcMat(height, width, CV_8UC4, src);
	cv::Mat dstMat;
	cv::medianBlur(srcMat, dstMat, size);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_meanBlur
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint size)
{
    jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	cv::Mat srcMat(height, width, CV_8UC4, src);
	cv::Mat dstMat;
	cv::blur(srcMat,dstMat,cv::Size(size,size),cv::Point(-1,-1));
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_gaussianBlur
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint size, jdouble sigma)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	cv::Mat srcMat(height, width, CV_8UC4, src);
	cv::Mat dstMat;
	cv::GaussianBlur(srcMat, dstMat, cv::Size(size,size),sigma,sigma);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_sobel
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	cv::Mat srcMat(height, width, CV_8UC4, src);
	cv::Mat dstMat;
	cv::Sobel(srcMat, dstMat, srcMat.depth(), 1, 1);
	cv::convertScaleAbs(dstMat, dstMat);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_prewitt
(JNIEnv *, jclass, jintArray, jintArray, jint, jint)
{

}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_roberts
(JNIEnv *, jclass, jintArray, jintArray, jint, jint)
{
}