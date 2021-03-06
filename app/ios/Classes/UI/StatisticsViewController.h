//
//  StatisticsViewController.h
//  Carat
//
//  Created by Jarno Petteri Laitinen on 06/10/15.
//  Copyright © 2015 University of Helsinki. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "BaseViewController.h"
#import "MeasurementBar.h"
#import "LocalizedLabel.h"
#import "PopularModel.h"

@interface StatisticsViewController : BaseViewController <NSURLConnectionDataDelegate>
@property (retain, nonatomic) IBOutlet UILabel *genIntensiveDescLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *genIntensiveBar;
@property (retain, nonatomic) IBOutlet UILabel *androidIntensiveLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *androidIntensiveBar;
@property (retain, nonatomic) IBOutlet UILabel *iOSIntensiveDescLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *iOSIntensiveBar;
@property (retain, nonatomic) IBOutlet UILabel *allUsersIntensiveDescLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *allUsersIntensiveBar;
@property (retain, nonatomic) IBOutlet LocalizedLabel *wellBehivedLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *wellBehivedBar;
@property (retain, nonatomic) IBOutlet LocalizedLabel *bugsLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *bugBar;
@property (retain, nonatomic) IBOutlet LocalizedLabel *hogsLabel;
@property (retain, nonatomic) IBOutlet MeasurementBar *hogsBar;
@property (retain, nonatomic) IBOutlet UILabel *iosPopularModelsLabel;
@property (retain, nonatomic) IBOutlet UILabel *androidPopularModelLabel;
@property (retain, nonatomic) IBOutlet UIActivityIndicatorView* spinner;
@property (retain, nonatomic) IBOutlet UIView *spinnerBackGround;
@property (retain, nonatomic) IBOutlet UINavigationItem *navbarTitle;

- (IBAction)showWellBehivedInfo:(id)sender;
- (IBAction)showBugsInfo:(id)sender;
- (IBAction)showHogsInfo:(id)sender;

@property (retain, nonatomic) IBOutlet UILabel *popularDeviceModelsDescLabel;

@end
