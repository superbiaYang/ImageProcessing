//
// Created by SuperbiaYang on 2016/6/3.
//

#include "processor_OpenCV_GrayscaleMorphology.h"
#include <opencv2/opencv.hpp>
using namespace cv;

JNIEXPORT void JNICALL Java_processor_OpenCV_GrayscaleMorphology_standardGradient
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat erodeMat, dilateMat, dstMat;
    Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
    erode(srcMat, erodeMat, element);
    dilate(srcMat, dilateMat, element);
    dstMat = (dilateMat - erodeMat) / 2;
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_GrayscaleMorphology_externalGradient
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat dilateMat, dstMat;
    Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
    dilate(srcMat, dilateMat, element);
    dstMat = (dilateMat - srcMat) / 2;
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_GrayscaleMorphology_internalGradient
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat erodeMat, dstMat;
    Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
    erode(srcMat, erodeMat, element);
    dstMat = (srcMat - erodeMat) / 2;
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_GrayscaleMorphology_OBR
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
    erode(srcMat, srcMat, element);
    dilate(srcMat, srcMat, element);
    Mat dstMat = srcMat.clone();
    Mat dilateMat = srcMat.clone();
    Mat resultMat = srcMat.clone();

    bool flag = false;
    do
    {
        dilate(resultMat, dilateMat, element);
        uchar* srcPtr = srcMat.ptr(0);
        uchar* dstPtr = dstMat.ptr(0);
        uchar* dilatePtr = dilateMat.ptr(0);
        uchar* resultPtr = resultMat.ptr(0);
        flag = false;
        for (int i = 0; i < width * height; ++i)
        {
            *resultPtr = *dilatePtr > *srcPtr ? *srcPtr : *dilatePtr;
            if (*dstPtr != *resultPtr)
            {
                flag = true;
            }
            *dstPtr = *resultPtr;
            ++srcPtr;
            ++dstPtr;
            ++resultPtr;
            ++dilatePtr;
        }
    } while(flag);
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_GrayscaleMorphology_CBR
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height, jint elemSize, jint elemType)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat element = getStructuringElement(elemType, Size(elemSize, elemSize), Point(elemSize / 2, elemSize / 2));
    dilate(srcMat, srcMat, element);
    erode(srcMat, srcMat, element);
    Mat dstMat = srcMat.clone();
    Mat dilateMat = srcMat.clone();
    Mat resultMat = srcMat.clone();

    bool flag = false;
    do
    {
        dilate(resultMat, dilateMat, element);
        uchar* srcPtr = srcMat.ptr(0);
        uchar* dstPtr = dstMat.ptr(0);
        uchar* dilatePtr = dilateMat.ptr(0);
        uchar* resultPtr = resultMat.ptr(0);
        flag = false;
        for (int i = 0; i < width * height; ++i)
        {
            *resultPtr = *dilatePtr > *srcPtr ? *srcPtr : *dilatePtr;
            if (*dstPtr != *resultPtr)
            {
                flag = true;
            }
            *dstPtr = *resultPtr;
            ++srcPtr;
            ++dstPtr;
            ++resultPtr;
            ++dilatePtr;
        }
    } while(flag);
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}