//
//  ProcessViewController.h
//  Carat
//
//  Created by Jarno Petteri Laitinen on 13/10/15.
//  Copyright © 2015 University of Helsinki. All rights reserved.
//

#import <UIKit/UIKit.h>
#import "WebInfoViewController.h"
#import "BaseViewController.h"
#import "BugHogTableViewCell.h"
#import "ListNetworkBaseViewController.h"

//#import "UIImageView+WebCache.h"
#import "UIImageDoNotCache.h" 

@interface ProcessViewController : ListNetworkBaseViewController{
    NSArray *processList;
}
@property (retain, nonatomic) IBOutlet UINavigationItem *navigationBar;
@property (retain, nonatomic) NSArray *processList;
@property (retain, nonatomic) NSDictionary *processInfos;
@end
