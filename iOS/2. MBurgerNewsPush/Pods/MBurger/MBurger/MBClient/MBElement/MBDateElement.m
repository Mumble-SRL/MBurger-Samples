//
//  MBDateElement.m
//  MBurger
//
//  Copyright © 2018 Mumble s.r.l. (https://mumbleideas.it/).
//  All rights reserved.
//

#import "MBDateElement.h"

@implementation MBDateElement

- (instancetype) initWithElementId: (NSInteger) elementId Name: (NSString *) name Order: (NSInteger) order Date: (NSDate *) date{
    self = [super initWithElementId:elementId Name:name Order:order Type:MBElementTypeDate];
    if (self){
        self.date = date;
    }
    return self;
}

- (instancetype) initWithDictionary: (NSDictionary *) dictionary{
    NSInteger elementId = [dictionary[@"id"] integerValue];
    NSString *name = dictionary[@"name"];
    NSInteger order = [dictionary[@"order"] integerValue];
    NSDateFormatter *dateFormatter = [[NSDateFormatter alloc] init];
    dateFormatter.dateFormat = @"dd/MM/yyyy HH:mm";
    NSString *dateString = dictionary[@"value"];
    NSDate *date = [dateFormatter dateFromString:dateString];
    return [self initWithElementId:elementId Name:name Order:order Date:date];
}

#pragma mark - Value

- (id) value {
    return self.date;
}

#pragma mark - NSCoding-NSSecureCoding

- (instancetype)initWithCoder:(NSCoder *)aDecoder{
    self = [super initWithCoder:aDecoder];
    if (self){
        self.date = [aDecoder decodeObjectOfClass:NSDate.class forKey:@"date"];
    }
    return self;
}

- (void)encodeWithCoder:(NSCoder *)aCoder{
    [super encodeWithCoder:aCoder];
    [aCoder encodeObject:_date forKey:@"date"];
}

+ (BOOL) supportsSecureCoding {
    return TRUE;
}

@end
