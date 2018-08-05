#include <jni.h>
#include <string>
#include "opencv2/imgproc.hpp"
#include "opencv2/imgcodecs.hpp"
#include "opencv2/highgui.hpp"
#include <iostream>
#include <cmath>
#include <android/log.h>

#define LOG_TAG "KVNC"

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jdouble

JNICALL
Java_com_example_kevin_hyperbilirubinemia_ImageProcessingService_ImageProcessing(
        JNIEnv *env,
        jobject /* this */,
        jlong image) {

    Mat& rawImage = *(Mat*) image;
    Mat preProcessedImage;z
    Mat labImage;
    Mat claheImage;
    Mat reshapeImage;
    Mat reshapeImage32f;


    Mat labelsIDX;
    Mat centers;
    TermCriteria criteria{TermCriteria::COUNT + TermCriteria::EPS, 20, 1.0};
    jint clusterNumber = 3;
    jint attempts = 5;
    jint flag = KMEANS_PP_CENTERS;

    vector<Mat> rawImageChannels(3);
    vector<Mat> finalImageChannels;
    vector<Mat> preProcessedImageChannels(3);
    split(rawImage, rawImageChannels);

    for (int i = 0; i < 3; i++) {
        medianBlur(rawImageChannels[i], preProcessedImageChannels[i], 5);
        finalImageChannels.push_back(preProcessedImageChannels[i]);
    }
    merge(&finalImageChannels[0],finalImageChannels.size(), preProcessedImage);

    cvtColor(preProcessedImage, labImage, CV_BGR2Lab);

    vector<Mat> labImageChannels(3);

    Mat claheL;
    split(labImage, labImageChannels);

    Ptr<CLAHE> clahe = createCLAHE();
    clahe->setClipLimit(2.0);
    clahe->setTilesGridSize(Size(10, 10));
    clahe->apply(labImageChannels[0], claheL);
    claheL.copyTo(labImageChannels[0]);
    merge(&labImageChannels[0], labImageChannels.size(), claheImage);

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "merged");
    cvtColor(claheImage, claheImage, CV_Lab2BGR);

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "clahed");
    Mat claheSegmenteation;
    cvtColor(claheImage, claheSegmenteation, CV_BGR2Lab);

    vector<Mat> segmentationChannels(3);
    vector<Mat> abChannels;
    split(claheSegmenteation, segmentationChannels);

    abChannels.push_back(segmentationChannels[1]);
    abChannels.push_back(segmentationChannels[2]);

    Mat abImage;
    merge(abChannels, abImage);

    //1 channel with (cols*rows) rows, the columns are a and b colour spaces
    reshapeImage = abImage.reshape(1, abImage.cols * abImage.rows);
    reshapeImage.convertTo(reshapeImage32f, CV_32FC1, 1.0 / 255.0);


    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "kmeans");
    kmeans(reshapeImage32f, clusterNumber, labelsIDX, criteria, attempts, flag, centers);
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "kmeaned");

    MatIterator_<Vec3b> startIterator;
    MatIterator_<Vec3b> endIterator;
    MatConstIterator_<jint> labelIndex = labelsIDX.begin<jint>();

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "iterating");
    for (startIterator = claheImage.begin<Vec3b>(),
                 endIterator = claheImage.end<Vec3b>();
         startIterator != endIterator; ++startIterator) {
        if (*labelIndex != 1) {
            *startIterator = 0;
            labelIndex++;

        } else {
            labelIndex++;
        }

    }

    Mat rgbImage = claheImage;
    Mat hsvImage;
    Mat ycrcbImage;

    cvtColor(rgbImage, hsvImage, CV_BGR2HSV);
    cvtColor(rgbImage, ycrcbImage, CV_BGR2YCrCb);


    vector<Vec3b> rgbPixel, hsvPixel, ycrcbPixel;
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "inputting vector to channels");
    for (int i = 0; i < rgbImage.rows; i++) {
        for (int j = 0; j < rgbImage.cols; j++) {
            rgbPixel.push_back(rgbImage.at<Vec3b>(i, j));
        }
    }

    for (int i = 0; i < hsvImage.rows; i++) {
        for (int j = 0; j < hsvImage.cols; j++) {
            hsvPixel.push_back(hsvImage.at<Vec3b>(i, j));
        }
    }

    for (int i = 0; i < ycrcbImage.rows; i++) {
        for (int j = 0; j < ycrcbImage.cols; j++) {
            ycrcbPixel.push_back(ycrcbImage.at<Vec3b>(i, j));
        }
    }


    jdouble rgbStat[4][3] = {0};
    jdouble hsvStat[4][3] = {0};
    jdouble ycrcbStat[4][3] = {0};
    jdouble sumRGB = 0, sumHSV = 0, sumYCrCb = 0, avgRGB = 0, avgHSV = 0, avgYCrCb = 0;


    jint size = rgbImage.cols * rgbImage.rows;

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "calculating mean");
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < size; j++) {
            sumRGB = rgbPixel[j].val[i] + sumRGB;
            sumHSV = hsvPixel[j].val[i] + sumHSV;
            sumYCrCb = ycrcbPixel[j].val[i] + sumYCrCb;
        }
        avgRGB = sumRGB / size;
        avgHSV = sumHSV / size;
        avgYCrCb = sumYCrCb / size;

        rgbStat[0][i] = avgRGB;
        hsvStat[0][i] = avgHSV;
        ycrcbStat[0][i] = avgYCrCb;

        sumRGB = 0;
        sumHSV = 0;
        sumYCrCb = 0;
    }

    jdouble diffRGB = 0, diffHSV = 0, diffYCrCb = 0;
    jdouble sumDiffRGB = 0, sumDiffHSV = 0, sumDiffYCrCb = 0;
    jdouble stdRGB = 0, stdHSV = 0, stdYCrCb = 0;
    jdouble skewRGB = 0, skewHSV = 0, skewYCrCb = 0;
    jdouble kurtRGB = 0, kurtHSV = 0, kurtYCrCb = 0;

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "std");
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < size; j++) {
            diffRGB = (rgbPixel[j].val[i] - rgbStat[0][i]); // - mean
            sumDiffRGB = (diffRGB * diffRGB) + sumDiffRGB;

            diffHSV = (hsvPixel[j].val[i] - hsvStat[0][i]);
            sumDiffHSV = (diffHSV * diffHSV) + sumDiffHSV;

            diffYCrCb = (ycrcbPixel[j].val[i] - ycrcbStat[0][i]);
            sumDiffYCrCb = (diffYCrCb * diffYCrCb) + sumDiffYCrCb;
        }
        stdRGB = sqrt(sumDiffRGB / size);
        stdHSV = sqrt(sumDiffHSV / size);
        stdYCrCb = sqrt(sumDiffYCrCb / size);

        rgbStat[1][i] = stdRGB;
        hsvStat[1][i] = stdHSV;
        ycrcbStat[1][i] = stdYCrCb;

        sumDiffHSV = 0, sumDiffRGB = 0, sumDiffYCrCb = 0;
    }
    jdouble sumDiffFourRGB = 0, sumDiffFourHSV = 0, sumDiffFourYCrCb = 0;

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "kurt and skew");
    for (int i = 0; i < 3; i++) {
        for (int j = 0; j < size; j++) {
            diffRGB = (rgbPixel[j].val[i] - rgbStat[0][i]);
            sumDiffRGB = (diffRGB * diffRGB * diffRGB) + sumDiffRGB;
            sumDiffFourRGB = (diffRGB * diffRGB * diffRGB * diffRGB) + sumDiffRGB;

            diffHSV = (hsvPixel[j].val[i] - hsvStat[0][i]);
            sumDiffHSV = (diffHSV * diffHSV * diffHSV) + sumDiffHSV;
            sumDiffFourHSV = (diffHSV * diffHSV * diffHSV * diffHSV) + sumDiffHSV;

            diffYCrCb = (ycrcbPixel[j].val[i] - ycrcbStat[0][i]);
            sumDiffYCrCb = (diffYCrCb * diffYCrCb * diffYCrCb) + sumDiffYCrCb;
            sumDiffFourYCrCb = (diffYCrCb * diffYCrCb * diffYCrCb * diffYCrCb) + sumDiffYCrCb;
        }

        skewRGB = sumDiffRGB / (rgbStat[1][i] * rgbStat[1][i] * rgbStat[1][i] * size); // div by std
        skewHSV = sumDiffHSV / (hsvStat[1][i] * hsvStat[1][i] * hsvStat[1][i] * size);
        skewYCrCb = sumDiffYCrCb / (ycrcbStat[1][i] * ycrcbStat[1][i] * ycrcbStat[1][i] * size);

        kurtRGB = sumDiffFourRGB /
                  (rgbStat[1][i] * rgbStat[1][i] * rgbStat[1][i] * rgbStat[1][i] * size);
        kurtHSV = sumDiffFourHSV /
                  (hsvStat[1][i] * hsvStat[1][i] * hsvStat[1][i] * hsvStat[1][i] * size);
        kurtYCrCb = sumDiffFourYCrCb /
                    (ycrcbStat[1][i] * ycrcbStat[1][i] * ycrcbStat[1][i] * ycrcbStat[1][i] * size);

        rgbStat[2][i] = skewRGB;
        rgbStat[3][i] = kurtRGB;
        hsvStat[2][i] = skewHSV;
        hsvStat[3][i] = kurtHSV;
        ycrcbStat[2][i] = skewYCrCb;
        ycrcbStat[3][i] = kurtYCrCb;

        sumDiffHSV = 0, sumDiffRGB = 0, sumDiffYCrCb = 0;
    }
/*
    cout << "RGB" << endl;
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 3; j++) {
            cout << rgbStat[i][j] << " ";
        }
        cout << endl;
    }


    cout << "HSV" << endl;
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 3; j++) {
            cout << hsvStat[i][j] << " ";
        }
        cout << endl;
    }


    cout << "YCrCb" << endl;
    for (int i = 0; i < 4; i++) {
        for (int j = 0; j < 3; j++) {
            cout << ycrcbStat[i][j] << " ";
        }
        cout << endl;
    }
*/
    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                        "calculate bilirubin");
    //std Cb, Kurt Cr, Kurt H, Kurt Cb, Mean H
    //0 : mean, 1 : std, 2: skew, 3 : kurt
    //0 : y, 1: cr, 2: cb
    jdouble y = ((0.28*ycrcbStat[1][2])- (1.86*ycrcbStat[3][1]) + (0.0061*hsvStat[3][0]) - (1.43*ycrcbStat[3][2]) +
                 (8.39*hsvStat[0][0]) + 19.13)/3.03225;

    __android_log_print(ANDROID_LOG_DEBUG, LOG_TAG,
                         "result = %f",y);
    /*  cout << "Kadar  BIlirubin " << y;

      cout << "a";
      int num;
      cin >> num;
      if (num == 0) {
          return 0;
      }
      */

    return y;
/*
string hello = "Hello from C++";
return env->NewStringUTF(hello.c_str());
*/
}
