//
// Created by SuperbiaYang on 2016/5/30.
//
#include "processor_OpenCV_Filter.h"
#include <opencv2/opencv.hpp>
using namespace cv;

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_medianBlur
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint size)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	medianBlur(srcMat, dstMat, size);
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
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	blur(srcMat,dstMat,Size(size,size),Point(-1,-1));
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
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat;
	GaussianBlur(srcMat, dstMat, Size(size,size),sigma,sigma);
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
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat_x, dstMat_y, dstMat;
	cvtColor(srcMat, srcMat, CV_RGB2GRAY);
	Sobel(srcMat, dstMat_x, CV_16S, 1, 0);
	Sobel(srcMat, dstMat_y, CV_16S, 0, 1);
	convertScaleAbs(dstMat_x, dstMat_x);
	convertScaleAbs(dstMat_y, dstMat_y);
	addWeighted(dstMat_x, 0.5, dstMat_y, 0.5, 0, dstMat);
	cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_prewitt
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	Mat dstMat_x, dstMat_y, dstMat;
	Mat kernel_x = (Mat_<double>(3,3) << 1, 1, 1, 0, 0, 0, -1, -1, -1);  
    Mat kernel_y = (Mat_<double>(3,3) << -1, 0, 1, -1, 0, 1, -1, 0, 1); 
	cvtColor(srcMat, srcMat, CV_RGB2GRAY);
	filter2D(srcMat, dstMat_x, CV_16S , kernel_x, Point(-1,-1));
	filter2D(srcMat, dstMat_y, CV_16S , kernel_y, Point(-1,-1));
	convertScaleAbs(dstMat_x, dstMat_x);
	convertScaleAbs(dstMat_y, dstMat_y);
	addWeighted(dstMat_x, 0.5, dstMat_y, 0.5, 0, dstMat);
	cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_Filter_roberts
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
	jboolean copy = false;
	jint* src = env->GetIntArrayElements(srcArray, &copy);
	jint* dst = env->GetIntArrayElements(dstArray, &copy);
	Mat srcMat(height, width, CV_8UC4, src);
	cvtColor(srcMat, srcMat, CV_RGB2GRAY);
	Mat dstMat = srcMat.clone();
	for (int y = 0; y < height - 1; ++y)
	{
		for (int x = 0; x < width - 1; ++x)
		{
			int p00 = srcMat.at<uchar>(x, y);
			int p01 = srcMat.at<uchar>(x, y + 1);
			int p10 = srcMat.at<uchar>(x + 1, y);
			int p11 = srcMat.at<uchar>(x + 1, y + 1);
			int temp = abs(p00 - p11);
			int temp1 = abs(p10 - p01);
			temp = (temp > temp1 ? temp : temp1);
			temp = (int)sqrt(float(temp * temp) + float(temp1 * temp1));
			dstMat.at<uchar>(x, y) = temp;
		}
	}
	cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
	memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
	env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
	env->ReleaseIntArrayElements(dstArray, dst, 0);
}