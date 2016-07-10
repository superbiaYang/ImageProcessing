//
// Created by SuperbiaYang on 2016/6/2.
//

#include "processor_OpenCV_BinaryMorphology.h"
#include <opencv2/opencv.hpp>
using namespace cv;

#define ELEMENT getStructuringElement(MORPH_CROSS, Size(3, 3), Point(1, 1))

JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_distanceTransform
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    Mat dstMat;
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    distanceTransform(srcMat, dstMat, CV_DIST_L2, 3);
    normalize(dstMat, dstMat, 0, 255, NORM_MINMAX);
    uchar* ptr = dstMat.ptr(0);
    for (int i = 0; i < width * height; ++i)
    {
        float* gray_ptr = reinterpret_cast<float*>(ptr);
        int gray = *gray_ptr;
        *ptr++ = gray;
        *ptr++ = gray;
        *ptr++ = gray;
        *ptr++ = 0xFF;
    }
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_skeleton
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jintArray skeletonArray, jint width, jint height)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    jint* skeleton = env->GetIntArrayElements(skeletonArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat dstMat(height, width, CV_8UC1, 0);
    Mat erodeMat, openMat, subMat;
    bool flag = false;
    int count = 1;
    do {
        flag = false;
        erode(srcMat, erodeMat, ELEMENT);
        erode(erodeMat, openMat, ELEMENT);
        dilate(openMat, openMat, ELEMENT);
        subMat = erodeMat - openMat;
        dstMat = subMat + dstMat;
        srcMat = erodeMat;

        uchar* ptr = srcMat.ptr(0);
        uchar* subPtr = subMat.ptr(0);
        for (int i = 0; i < width * height; ++i)
        {
            if (*ptr != 0) {
                flag = true;
            }
            ++ptr;
            if (*subPtr != 0) {
                skeleton[i] = count;
            }
            ++subPtr;
        }
        ++count;
    } while (flag);
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
    env->ReleaseIntArrayElements(skeletonArray, skeleton, 0);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_reconstruct
(JNIEnv *env, jclass, jintArray skeletonArray, jintArray dstArray, jint width, jint height)
{
    jboolean copy = false;
    jint* skeleton = env->GetIntArrayElements(skeletonArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);

    Mat dstMat = Mat::zeros(height,width,CV_8UC1);

    int maxSkeleton = 0;
    for (int i = 0; i < width * height; ++i)
    {
        maxSkeleton = std::max(maxSkeleton, skeleton[i]);
    }

    for (int v = maxSkeleton; v > 0; --v)
    {
        Mat subMat = Mat::zeros(height,width,CV_8UC1);
        uchar* ptr = subMat.ptr(0);
        for (int i = 0; i < width * height; ++i)
        {
            ptr[i] = (skeleton[i] == v ? 255 : 0);
        }
        dstMat = dstMat + subMat;
        dilate(dstMat, dstMat, ELEMENT);
    }
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));

    env->ReleaseIntArrayElements(dstArray, dst, 0);
    env->ReleaseIntArrayElements(skeletonArray, skeleton, JNI_ABORT);
}

JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_conditionalDilation
(JNIEnv *env, jclass, jintArray srcArray, jintArray maskArray, jintArray dstArray, jint width, jint height)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    jint* mask = env->GetIntArrayElements(maskArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat maskMat(height, width, CV_8UC4, mask);
    cvtColor(maskMat, maskMat, CV_RGB2GRAY);

    Mat dilateMat = srcMat.clone();
    Mat dstMat = srcMat.clone();
    Mat resultMat = srcMat.clone();

    bool flag = true;
    do
    {
        dilate(resultMat, dilateMat, ELEMENT);
        dilateMat.copyTo(resultMat, maskMat);

        flag = false;
        uchar* ptr = dstMat.ptr(0);
        uchar* resultPtr = resultMat.ptr(0);
        for (int i = 0; i < width * height; ++i)
        {
            if (*ptr != *resultPtr)
            {
                flag = true;
                break;
            }
            ++ptr;
            ++resultPtr;
        }
        dstMat = resultMat.clone();
    } while(flag);
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(dstArray, dst, 0);
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(maskArray, mask, JNI_ABORT);
}


/*
 * Class:     processor_OpenCV_BinaryMorphology
 * Method:    standardEdge
 * Signature: ([I[III)V
 */
JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_standardEdge
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat erodeMat, dilateMat, dstMat;
    erode(srcMat, erodeMat, ELEMENT);
    dilate(srcMat, dilateMat, ELEMENT);
    dstMat = dilateMat - erodeMat;
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

/*
 * Class:     processor_OpenCV_BinaryMorphology
 * Method:    externalEdge
 * Signature: ([I[III)V
 */
JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_externalEdge
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat dilateMat, dstMat;
    dilate(srcMat, dilateMat, ELEMENT);
    dstMat = dilateMat - srcMat;
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}

/*
 * Class:     processor_OpenCV_BinaryMorphology
 * Method:    internalEdge
 * Signature: ([I[III)V
 */
JNIEXPORT void JNICALL Java_processor_OpenCV_BinaryMorphology_internalEdge
(JNIEnv *env, jclass, jintArray srcArray, jintArray dstArray, jint width, jint height)
{
    jboolean copy = false;
    jint* src = env->GetIntArrayElements(srcArray, &copy);
    jint* dst = env->GetIntArrayElements(dstArray, &copy);
    Mat srcMat(height, width, CV_8UC4, src);
    cvtColor(srcMat, srcMat, CV_RGB2GRAY);
    Mat erodeMat, dstMat;
    erode(srcMat, erodeMat, ELEMENT);
    dstMat = srcMat - erodeMat;
    cvtColor(dstMat, dstMat, CV_GRAY2RGBA);
    memcpy(dst, dstMat.ptr(0), width * height * sizeof(jint));
    env->ReleaseIntArrayElements(srcArray, src, JNI_ABORT);
    env->ReleaseIntArrayElements(dstArray, dst, 0);
}