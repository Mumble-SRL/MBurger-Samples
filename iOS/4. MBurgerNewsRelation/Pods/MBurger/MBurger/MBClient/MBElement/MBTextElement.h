//
//  MBTextElement.h
//  MBurger
//
//  Copyright © 2018 Mumble s.r.l. (https://mumbleideas.it/).
//  All rights reserved.
//

#import "MBElement.h"

/**
 This class represents a MBurger text element.
 It is the counterpart of the 'text' and 'textelement' types on the dashboard
 */
@interface MBTextElement : MBElement <NSCoding, NSSecureCoding>

/**
 The value of the element.
 */
@property (nonatomic, retain, nullable) NSString *text;

/**
 Initializes a text element with an elementId and its value.
 @see This function calls the super initializer `-[MBElement initWithElementId:Name:Order:Type:]`

 @param elementId The id of the element.
 @param name The name of the element.
 @param order The order of the element.
 @param text The text of the element.
 
 @return A newly created MBTextElement initialized with the elementId, the name and the value passed.
*/
- (nonnull instancetype) initWithElementId: (NSInteger) elementId Name: (nonnull NSString *) name Order: (NSInteger) order Text: (nullable NSString *) text;

@end
