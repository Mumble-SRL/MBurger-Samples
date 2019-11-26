#ifdef __OBJC__
#import <UIKit/UIKit.h>
#else
#ifndef FOUNDATION_EXPORT
#if defined(__cplusplus)
#define FOUNDATION_EXPORT extern "C"
#else
#define FOUNDATION_EXPORT extern
#endif
#endif
#endif

#import "MBurger.h"
#import "MBAdmin.h"
#import "MBUploadableCheckboxElement.h"
#import "MBUploadableElement.h"
#import "MBUploadableElementsFactory.h"
#import "MBUploadableFilesElement.h"
#import "MBUploadableImagesElement.h"
#import "MBUploadableTextElement.h"
#import "MBApiManager.h"
#import "MBHTTPMethod.h"
#import "MBMultipartForm.h"
#import "MBResponse.h"
#import "MBAuth.h"
#import "MBAuthContractAcceptanceParameter.h"
#import "MBUser.h"
#import "MBUserContractStatus.h"
#import "MBUserPublishedSection.h"
#import "MBBlock.h"
#import "MBClient.h"
#import "MBAddressElement.h"
#import "MBCheckboxElement.h"
#import "MBDateElement.h"
#import "MBDropdownElement.h"
#import "MBDropdownElementOption.h"
#import "MBElement.h"
#import "MBElementsFactory.h"
#import "MBFile.h"
#import "MBGeneralElement.h"
#import "MBImage.h"
#import "MBImagesElement.h"
#import "MBMarkdownElement.h"
#import "MBMediaElement.h"
#import "MBPollElement.h"
#import "MBRelationElement.h"
#import "MBTextElement.h"
#import "MBWysiwygElement.h"
#import "MBPaginationInfo.h"
#import "MBFilterParameter.h"
#import "MBGeneralParameter.h"
#import "MBGeofenceParameter.h"
#import "MBPaginationParameter.h"
#import "MBParameter.h"
#import "MBSortParameter.h"
#import "MBEvidenceInformations.h"
#import "MBLegalContract.h"
#import "MBProject.h"
#import "MBSection.h"
#import "MBManager.h"
#import "MBUtilities.h"
#import "MBPlugin.h"

FOUNDATION_EXPORT double MBurgerVersionNumber;
FOUNDATION_EXPORT const unsigned char MBurgerVersionString[];

