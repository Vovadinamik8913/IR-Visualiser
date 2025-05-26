; ModuleID = '2.c'
source_filename = "2.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

%struct.my_arr = type { i32*, i32 }
%struct.Node = type { i32, i32, i32, %struct.Node*, %struct.Node* }
%struct._IO_FILE = type { i32, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, i8*, %struct._IO_marker*, %struct._IO_FILE*, i32, i32, i64, i16, i8, [1 x i8], i8*, i64, %struct._IO_codecvt*, %struct._IO_wide_data*, %struct._IO_FILE*, i8*, i64, i32, [20 x i8] }
%struct._IO_marker = type opaque
%struct._IO_codecvt = type opaque
%struct._IO_wide_data = type opaque

@.str = private unnamed_addr constant [10 x i8] c"input.bin\00", align 1
@.str.1 = private unnamed_addr constant [3 x i8] c"rb\00", align 1
@.str.2 = private unnamed_addr constant [11 x i8] c"output.bin\00", align 1
@.str.3 = private unnamed_addr constant [3 x i8] c"wb\00", align 1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @max(i32 noundef %0, i32 noundef %1) #0 {
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  store i32 %0, i32* %4, align 4
  store i32 %1, i32* %5, align 4
  %6 = load i32, i32* %4, align 4
  %7 = load i32, i32* %5, align 4
  %8 = icmp sge i32 %6, %7
  br i1 %8, label %9, label %11

9:                                                ; preds = %2
  %10 = load i32, i32* %4, align 4
  store i32 %10, i32* %3, align 4
  br label %13

11:                                               ; preds = %2
  %12 = load i32, i32* %5, align 4
  store i32 %12, i32* %3, align 4
  br label %13

13:                                               ; preds = %11, %9
  %14 = load i32, i32* %3, align 4
  ret i32 %14
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @swap(i32* noundef %0, i32* noundef %1) #0 {
  %3 = alloca i32*, align 8
  %4 = alloca i32*, align 8
  %5 = alloca i32, align 4
  store i32* %0, i32** %3, align 8
  store i32* %1, i32** %4, align 8
  %6 = load i32*, i32** %3, align 8
  %7 = load i32, i32* %6, align 4
  store i32 %7, i32* %5, align 4
  %8 = load i32*, i32** %4, align 8
  %9 = load i32, i32* %8, align 4
  %10 = load i32*, i32** %3, align 8
  store i32 %9, i32* %10, align 4
  %11 = load i32, i32* %5, align 4
  %12 = load i32*, i32** %4, align 8
  store i32 %11, i32* %12, align 4
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @shuffle(i32* noundef %0, i32 noundef %1) #0 {
  %3 = alloca i32*, align 8
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  store i32* %0, i32** %3, align 8
  store i32 %1, i32* %4, align 4
  %7 = call i64 @time(i64* noundef null) #4
  %8 = trunc i64 %7 to i32
  call void @srand(i32 noundef %8) #4
  store i32 0, i32* %5, align 4
  br label %9

9:                                                ; preds = %25, %2
  %10 = load i32, i32* %5, align 4
  %11 = load i32, i32* %4, align 4
  %12 = icmp slt i32 %10, %11
  br i1 %12, label %13, label %28

13:                                               ; preds = %9
  %14 = call i32 @rand() #4
  %15 = load i32, i32* %4, align 4
  %16 = srem i32 %14, %15
  store i32 %16, i32* %6, align 4
  %17 = load i32*, i32** %3, align 8
  %18 = load i32, i32* %5, align 4
  %19 = sext i32 %18 to i64
  %20 = getelementptr inbounds i32, i32* %17, i64 %19
  %21 = load i32*, i32** %3, align 8
  %22 = load i32, i32* %6, align 4
  %23 = sext i32 %22 to i64
  %24 = getelementptr inbounds i32, i32* %21, i64 %23
  call void @swap(i32* noundef %20, i32* noundef %24)
  br label %25

25:                                               ; preds = %13
  %26 = load i32, i32* %5, align 4
  %27 = add nsw i32 %26, 1
  store i32 %27, i32* %5, align 4
  br label %9, !llvm.loop !6

28:                                               ; preds = %9
  ret void
}

; Function Attrs: nounwind
declare void @srand(i32 noundef) #1

; Function Attrs: nounwind
declare i64 @time(i64* noundef) #1

; Function Attrs: nounwind
declare i32 @rand() #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @add_my_arr(%struct.my_arr* noundef %0, i32 noundef %1) #0 {
  %3 = alloca %struct.my_arr*, align 8
  %4 = alloca i32, align 4
  store %struct.my_arr* %0, %struct.my_arr** %3, align 8
  store i32 %1, i32* %4, align 4
  %5 = load i32, i32* %4, align 4
  %6 = load %struct.my_arr*, %struct.my_arr** %3, align 8
  %7 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %6, i32 0, i32 0
  %8 = load i32*, i32** %7, align 8
  %9 = load %struct.my_arr*, %struct.my_arr** %3, align 8
  %10 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %9, i32 0, i32 1
  %11 = load i32, i32* %10, align 8
  %12 = sext i32 %11 to i64
  %13 = getelementptr inbounds i32, i32* %8, i64 %12
  store i32 %5, i32* %13, align 4
  %14 = load %struct.my_arr*, %struct.my_arr** %3, align 8
  %15 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %14, i32 0, i32 1
  %16 = load i32, i32* %15, align 8
  %17 = add nsw i32 %16, 1
  store i32 %17, i32* %15, align 8
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local %struct.Node* @rotation_left(%struct.Node* noundef %0) #0 {
  %2 = alloca %struct.Node*, align 8
  %3 = alloca %struct.Node*, align 8
  store %struct.Node* %0, %struct.Node** %2, align 8
  %4 = load %struct.Node*, %struct.Node** %2, align 8
  %5 = getelementptr inbounds %struct.Node, %struct.Node* %4, i32 0, i32 4
  %6 = load %struct.Node*, %struct.Node** %5, align 8
  store %struct.Node* %6, %struct.Node** %3, align 8
  %7 = load %struct.Node*, %struct.Node** %2, align 8
  %8 = getelementptr inbounds %struct.Node, %struct.Node* %7, i32 0, i32 4
  %9 = load %struct.Node*, %struct.Node** %8, align 8
  %10 = getelementptr inbounds %struct.Node, %struct.Node* %9, i32 0, i32 3
  %11 = load %struct.Node*, %struct.Node** %10, align 8
  %12 = load %struct.Node*, %struct.Node** %2, align 8
  %13 = getelementptr inbounds %struct.Node, %struct.Node* %12, i32 0, i32 4
  store %struct.Node* %11, %struct.Node** %13, align 8
  %14 = load %struct.Node*, %struct.Node** %2, align 8
  %15 = load %struct.Node*, %struct.Node** %3, align 8
  %16 = getelementptr inbounds %struct.Node, %struct.Node* %15, i32 0, i32 3
  store %struct.Node* %14, %struct.Node** %16, align 8
  %17 = load %struct.Node*, %struct.Node** %3, align 8
  ret %struct.Node* %17
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local %struct.Node* @rotation_right(%struct.Node* noundef %0) #0 {
  %2 = alloca %struct.Node*, align 8
  %3 = alloca %struct.Node*, align 8
  store %struct.Node* %0, %struct.Node** %2, align 8
  %4 = load %struct.Node*, %struct.Node** %2, align 8
  %5 = getelementptr inbounds %struct.Node, %struct.Node* %4, i32 0, i32 3
  %6 = load %struct.Node*, %struct.Node** %5, align 8
  store %struct.Node* %6, %struct.Node** %3, align 8
  %7 = load %struct.Node*, %struct.Node** %2, align 8
  %8 = getelementptr inbounds %struct.Node, %struct.Node* %7, i32 0, i32 3
  %9 = load %struct.Node*, %struct.Node** %8, align 8
  %10 = getelementptr inbounds %struct.Node, %struct.Node* %9, i32 0, i32 4
  %11 = load %struct.Node*, %struct.Node** %10, align 8
  %12 = load %struct.Node*, %struct.Node** %2, align 8
  %13 = getelementptr inbounds %struct.Node, %struct.Node* %12, i32 0, i32 3
  store %struct.Node* %11, %struct.Node** %13, align 8
  %14 = load %struct.Node*, %struct.Node** %2, align 8
  %15 = load %struct.Node*, %struct.Node** %3, align 8
  %16 = getelementptr inbounds %struct.Node, %struct.Node* %15, i32 0, i32 4
  store %struct.Node* %14, %struct.Node** %16, align 8
  %17 = load %struct.Node*, %struct.Node** %3, align 8
  ret %struct.Node* %17
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @update_height(%struct.Node* noundef %0) #0 {
  %2 = alloca %struct.Node*, align 8
  %3 = alloca i32, align 4
  store %struct.Node* %0, %struct.Node** %2, align 8
  store i32 0, i32* %3, align 4
  %4 = load %struct.Node*, %struct.Node** %2, align 8
  %5 = getelementptr inbounds %struct.Node, %struct.Node* %4, i32 0, i32 3
  %6 = load %struct.Node*, %struct.Node** %5, align 8
  %7 = icmp ne %struct.Node* %6, null
  br i1 %7, label %8, label %16

8:                                                ; preds = %1
  %9 = load i32, i32* %3, align 4
  %10 = load %struct.Node*, %struct.Node** %2, align 8
  %11 = getelementptr inbounds %struct.Node, %struct.Node* %10, i32 0, i32 3
  %12 = load %struct.Node*, %struct.Node** %11, align 8
  %13 = getelementptr inbounds %struct.Node, %struct.Node* %12, i32 0, i32 1
  %14 = load i32, i32* %13, align 4
  %15 = call i32 @max(i32 noundef %9, i32 noundef %14)
  store i32 %15, i32* %3, align 4
  br label %16

16:                                               ; preds = %8, %1
  %17 = load %struct.Node*, %struct.Node** %2, align 8
  %18 = getelementptr inbounds %struct.Node, %struct.Node* %17, i32 0, i32 4
  %19 = load %struct.Node*, %struct.Node** %18, align 8
  %20 = icmp ne %struct.Node* %19, null
  br i1 %20, label %21, label %29

21:                                               ; preds = %16
  %22 = load i32, i32* %3, align 4
  %23 = load %struct.Node*, %struct.Node** %2, align 8
  %24 = getelementptr inbounds %struct.Node, %struct.Node* %23, i32 0, i32 4
  %25 = load %struct.Node*, %struct.Node** %24, align 8
  %26 = getelementptr inbounds %struct.Node, %struct.Node* %25, i32 0, i32 1
  %27 = load i32, i32* %26, align 4
  %28 = call i32 @max(i32 noundef %22, i32 noundef %27)
  store i32 %28, i32* %3, align 4
  br label %29

29:                                               ; preds = %21, %16
  %30 = load i32, i32* %3, align 4
  %31 = add nsw i32 %30, 1
  %32 = load %struct.Node*, %struct.Node** %2, align 8
  %33 = getelementptr inbounds %struct.Node, %struct.Node* %32, i32 0, i32 1
  store i32 %31, i32* %33, align 4
  ret void
}

; Function Attrs: noinline nounwind optnone uwtable
define dso_local %struct.Node* @rebalance(%struct.Node* noundef %0) #0 {
  %2 = alloca %struct.Node*, align 8
  %3 = alloca %struct.Node*, align 8
  %4 = alloca i32, align 4
  %5 = alloca i32, align 4
  %6 = alloca i32, align 4
  %7 = alloca i32, align 4
  %8 = alloca i32, align 4
  %9 = alloca i32, align 4
  store %struct.Node* %0, %struct.Node** %3, align 8
  %10 = load %struct.Node*, %struct.Node** %3, align 8
  %11 = getelementptr inbounds %struct.Node, %struct.Node* %10, i32 0, i32 3
  %12 = load %struct.Node*, %struct.Node** %11, align 8
  %13 = icmp eq %struct.Node* %12, null
  br i1 %13, label %14, label %15

14:                                               ; preds = %1
  br label %21

15:                                               ; preds = %1
  %16 = load %struct.Node*, %struct.Node** %3, align 8
  %17 = getelementptr inbounds %struct.Node, %struct.Node* %16, i32 0, i32 3
  %18 = load %struct.Node*, %struct.Node** %17, align 8
  %19 = getelementptr inbounds %struct.Node, %struct.Node* %18, i32 0, i32 1
  %20 = load i32, i32* %19, align 4
  br label %21

21:                                               ; preds = %15, %14
  %22 = phi i32 [ 0, %14 ], [ %20, %15 ]
  store i32 %22, i32* %5, align 4
  %23 = load %struct.Node*, %struct.Node** %3, align 8
  %24 = getelementptr inbounds %struct.Node, %struct.Node* %23, i32 0, i32 4
  %25 = load %struct.Node*, %struct.Node** %24, align 8
  %26 = icmp eq %struct.Node* %25, null
  br i1 %26, label %27, label %28

27:                                               ; preds = %21
  br label %34

28:                                               ; preds = %21
  %29 = load %struct.Node*, %struct.Node** %3, align 8
  %30 = getelementptr inbounds %struct.Node, %struct.Node* %29, i32 0, i32 4
  %31 = load %struct.Node*, %struct.Node** %30, align 8
  %32 = getelementptr inbounds %struct.Node, %struct.Node* %31, i32 0, i32 1
  %33 = load i32, i32* %32, align 4
  br label %34

34:                                               ; preds = %28, %27
  %35 = phi i32 [ 0, %27 ], [ %33, %28 ]
  store i32 %35, i32* %4, align 4
  %36 = load i32, i32* %5, align 4
  %37 = load i32, i32* %4, align 4
  %38 = sub nsw i32 %36, %37
  %39 = call i32 @abs(i32 noundef %38) #5
  %40 = icmp sle i32 %39, 1
  br i1 %40, label %41, label %44

41:                                               ; preds = %34
  %42 = load %struct.Node*, %struct.Node** %3, align 8
  call void @update_height(%struct.Node* noundef %42)
  %43 = load %struct.Node*, %struct.Node** %3, align 8
  store %struct.Node* %43, %struct.Node** %2, align 8
  br label %156

44:                                               ; preds = %34
  %45 = load i32, i32* %4, align 4
  %46 = load i32, i32* %5, align 4
  %47 = icmp sgt i32 %45, %46
  br i1 %47, label %48, label %101

48:                                               ; preds = %44
  %49 = load %struct.Node*, %struct.Node** %3, align 8
  %50 = getelementptr inbounds %struct.Node, %struct.Node* %49, i32 0, i32 4
  %51 = load %struct.Node*, %struct.Node** %50, align 8
  %52 = getelementptr inbounds %struct.Node, %struct.Node* %51, i32 0, i32 3
  %53 = load %struct.Node*, %struct.Node** %52, align 8
  %54 = icmp eq %struct.Node* %53, null
  br i1 %54, label %55, label %56

55:                                               ; preds = %48
  br label %64

56:                                               ; preds = %48
  %57 = load %struct.Node*, %struct.Node** %3, align 8
  %58 = getelementptr inbounds %struct.Node, %struct.Node* %57, i32 0, i32 4
  %59 = load %struct.Node*, %struct.Node** %58, align 8
  %60 = getelementptr inbounds %struct.Node, %struct.Node* %59, i32 0, i32 3
  %61 = load %struct.Node*, %struct.Node** %60, align 8
  %62 = getelementptr inbounds %struct.Node, %struct.Node* %61, i32 0, i32 1
  %63 = load i32, i32* %62, align 4
  br label %64

64:                                               ; preds = %56, %55
  %65 = phi i32 [ 0, %55 ], [ %63, %56 ]
  store i32 %65, i32* %6, align 4
  %66 = load %struct.Node*, %struct.Node** %3, align 8
  %67 = getelementptr inbounds %struct.Node, %struct.Node* %66, i32 0, i32 4
  %68 = load %struct.Node*, %struct.Node** %67, align 8
  %69 = getelementptr inbounds %struct.Node, %struct.Node* %68, i32 0, i32 4
  %70 = load %struct.Node*, %struct.Node** %69, align 8
  %71 = icmp eq %struct.Node* %70, null
  br i1 %71, label %72, label %73

72:                                               ; preds = %64
  br label %81

73:                                               ; preds = %64
  %74 = load %struct.Node*, %struct.Node** %3, align 8
  %75 = getelementptr inbounds %struct.Node, %struct.Node* %74, i32 0, i32 4
  %76 = load %struct.Node*, %struct.Node** %75, align 8
  %77 = getelementptr inbounds %struct.Node, %struct.Node* %76, i32 0, i32 4
  %78 = load %struct.Node*, %struct.Node** %77, align 8
  %79 = getelementptr inbounds %struct.Node, %struct.Node* %78, i32 0, i32 1
  %80 = load i32, i32* %79, align 4
  br label %81

81:                                               ; preds = %73, %72
  %82 = phi i32 [ 0, %72 ], [ %80, %73 ]
  store i32 %82, i32* %7, align 4
  %83 = load i32, i32* %6, align 4
  %84 = load i32, i32* %7, align 4
  %85 = add nsw i32 %84, 1
  %86 = icmp eq i32 %83, %85
  br i1 %86, label %87, label %94

87:                                               ; preds = %81
  %88 = load %struct.Node*, %struct.Node** %3, align 8
  %89 = getelementptr inbounds %struct.Node, %struct.Node* %88, i32 0, i32 4
  %90 = load %struct.Node*, %struct.Node** %89, align 8
  %91 = call %struct.Node* @rotation_right(%struct.Node* noundef %90)
  %92 = load %struct.Node*, %struct.Node** %3, align 8
  %93 = getelementptr inbounds %struct.Node, %struct.Node* %92, i32 0, i32 4
  store %struct.Node* %91, %struct.Node** %93, align 8
  br label %94

94:                                               ; preds = %87, %81
  %95 = load %struct.Node*, %struct.Node** %3, align 8
  %96 = call %struct.Node* @rotation_left(%struct.Node* noundef %95)
  store %struct.Node* %96, %struct.Node** %3, align 8
  %97 = load %struct.Node*, %struct.Node** %3, align 8
  %98 = getelementptr inbounds %struct.Node, %struct.Node* %97, i32 0, i32 3
  %99 = load %struct.Node*, %struct.Node** %98, align 8
  call void @update_height(%struct.Node* noundef %99)
  %100 = load %struct.Node*, %struct.Node** %3, align 8
  call void @update_height(%struct.Node* noundef %100)
  br label %154

101:                                              ; preds = %44
  %102 = load %struct.Node*, %struct.Node** %3, align 8
  %103 = getelementptr inbounds %struct.Node, %struct.Node* %102, i32 0, i32 3
  %104 = load %struct.Node*, %struct.Node** %103, align 8
  %105 = getelementptr inbounds %struct.Node, %struct.Node* %104, i32 0, i32 3
  %106 = load %struct.Node*, %struct.Node** %105, align 8
  %107 = icmp eq %struct.Node* %106, null
  br i1 %107, label %108, label %109

108:                                              ; preds = %101
  br label %117

109:                                              ; preds = %101
  %110 = load %struct.Node*, %struct.Node** %3, align 8
  %111 = getelementptr inbounds %struct.Node, %struct.Node* %110, i32 0, i32 3
  %112 = load %struct.Node*, %struct.Node** %111, align 8
  %113 = getelementptr inbounds %struct.Node, %struct.Node* %112, i32 0, i32 3
  %114 = load %struct.Node*, %struct.Node** %113, align 8
  %115 = getelementptr inbounds %struct.Node, %struct.Node* %114, i32 0, i32 1
  %116 = load i32, i32* %115, align 4
  br label %117

117:                                              ; preds = %109, %108
  %118 = phi i32 [ 0, %108 ], [ %116, %109 ]
  store i32 %118, i32* %8, align 4
  %119 = load %struct.Node*, %struct.Node** %3, align 8
  %120 = getelementptr inbounds %struct.Node, %struct.Node* %119, i32 0, i32 3
  %121 = load %struct.Node*, %struct.Node** %120, align 8
  %122 = getelementptr inbounds %struct.Node, %struct.Node* %121, i32 0, i32 4
  %123 = load %struct.Node*, %struct.Node** %122, align 8
  %124 = icmp eq %struct.Node* %123, null
  br i1 %124, label %125, label %126

125:                                              ; preds = %117
  br label %134

126:                                              ; preds = %117
  %127 = load %struct.Node*, %struct.Node** %3, align 8
  %128 = getelementptr inbounds %struct.Node, %struct.Node* %127, i32 0, i32 3
  %129 = load %struct.Node*, %struct.Node** %128, align 8
  %130 = getelementptr inbounds %struct.Node, %struct.Node* %129, i32 0, i32 4
  %131 = load %struct.Node*, %struct.Node** %130, align 8
  %132 = getelementptr inbounds %struct.Node, %struct.Node* %131, i32 0, i32 1
  %133 = load i32, i32* %132, align 4
  br label %134

134:                                              ; preds = %126, %125
  %135 = phi i32 [ 0, %125 ], [ %133, %126 ]
  store i32 %135, i32* %9, align 4
  %136 = load i32, i32* %9, align 4
  %137 = load i32, i32* %8, align 4
  %138 = add nsw i32 %137, 1
  %139 = icmp eq i32 %136, %138
  br i1 %139, label %140, label %153

140:                                              ; preds = %134
  %141 = load %struct.Node*, %struct.Node** %3, align 8
  %142 = getelementptr inbounds %struct.Node, %struct.Node* %141, i32 0, i32 3
  %143 = load %struct.Node*, %struct.Node** %142, align 8
  %144 = call %struct.Node* @rotation_left(%struct.Node* noundef %143)
  %145 = load %struct.Node*, %struct.Node** %3, align 8
  %146 = getelementptr inbounds %struct.Node, %struct.Node* %145, i32 0, i32 3
  store %struct.Node* %144, %struct.Node** %146, align 8
  %147 = load %struct.Node*, %struct.Node** %3, align 8
  %148 = call %struct.Node* @rotation_right(%struct.Node* noundef %147)
  store %struct.Node* %148, %struct.Node** %3, align 8
  %149 = load %struct.Node*, %struct.Node** %3, align 8
  %150 = getelementptr inbounds %struct.Node, %struct.Node* %149, i32 0, i32 4
  %151 = load %struct.Node*, %struct.Node** %150, align 8
  call void @update_height(%struct.Node* noundef %151)
  %152 = load %struct.Node*, %struct.Node** %3, align 8
  call void @update_height(%struct.Node* noundef %152)
  br label %153

153:                                              ; preds = %140, %134
  br label %154

154:                                              ; preds = %153, %94
  %155 = load %struct.Node*, %struct.Node** %3, align 8
  store %struct.Node* %155, %struct.Node** %2, align 8
  br label %156

156:                                              ; preds = %154, %41
  %157 = load %struct.Node*, %struct.Node** %2, align 8
  ret %struct.Node* %157
}

; Function Attrs: nounwind readnone willreturn
declare i32 @abs(i32 noundef) #2

; Function Attrs: noinline nounwind optnone uwtable
define dso_local %struct.Node* @add(%struct.Node* noundef %0, i32 noundef %1) #0 {
  %3 = alloca %struct.Node*, align 8
  %4 = alloca %struct.Node*, align 8
  %5 = alloca i32, align 4
  %6 = alloca %struct.Node*, align 8
  store %struct.Node* %0, %struct.Node** %4, align 8
  store i32 %1, i32* %5, align 4
  %7 = load %struct.Node*, %struct.Node** %4, align 8
  %8 = icmp eq %struct.Node* %7, null
  br i1 %8, label %9, label %20

9:                                                ; preds = %2
  %10 = call noalias i8* @calloc(i64 noundef 1, i64 noundef 32) #4
  %11 = bitcast i8* %10 to %struct.Node*
  store %struct.Node* %11, %struct.Node** %6, align 8
  %12 = load i32, i32* %5, align 4
  %13 = load %struct.Node*, %struct.Node** %6, align 8
  %14 = getelementptr inbounds %struct.Node, %struct.Node* %13, i32 0, i32 0
  store i32 %12, i32* %14, align 8
  %15 = load %struct.Node*, %struct.Node** %6, align 8
  %16 = getelementptr inbounds %struct.Node, %struct.Node* %15, i32 0, i32 1
  store i32 1, i32* %16, align 4
  %17 = load %struct.Node*, %struct.Node** %6, align 8
  %18 = getelementptr inbounds %struct.Node, %struct.Node* %17, i32 0, i32 2
  store i32 1, i32* %18, align 8
  %19 = load %struct.Node*, %struct.Node** %6, align 8
  store %struct.Node* %19, %struct.Node** %3, align 8
  br label %57

20:                                               ; preds = %2
  %21 = load %struct.Node*, %struct.Node** %4, align 8
  %22 = getelementptr inbounds %struct.Node, %struct.Node* %21, i32 0, i32 0
  %23 = load i32, i32* %22, align 8
  %24 = load i32, i32* %5, align 4
  %25 = icmp sgt i32 %23, %24
  br i1 %25, label %26, label %34

26:                                               ; preds = %20
  %27 = load %struct.Node*, %struct.Node** %4, align 8
  %28 = getelementptr inbounds %struct.Node, %struct.Node* %27, i32 0, i32 3
  %29 = load %struct.Node*, %struct.Node** %28, align 8
  %30 = load i32, i32* %5, align 4
  %31 = call %struct.Node* @add(%struct.Node* noundef %29, i32 noundef %30)
  %32 = load %struct.Node*, %struct.Node** %4, align 8
  %33 = getelementptr inbounds %struct.Node, %struct.Node* %32, i32 0, i32 3
  store %struct.Node* %31, %struct.Node** %33, align 8
  br label %54

34:                                               ; preds = %20
  %35 = load %struct.Node*, %struct.Node** %4, align 8
  %36 = getelementptr inbounds %struct.Node, %struct.Node* %35, i32 0, i32 0
  %37 = load i32, i32* %36, align 8
  %38 = load i32, i32* %5, align 4
  %39 = icmp slt i32 %37, %38
  br i1 %39, label %40, label %48

40:                                               ; preds = %34
  %41 = load %struct.Node*, %struct.Node** %4, align 8
  %42 = getelementptr inbounds %struct.Node, %struct.Node* %41, i32 0, i32 4
  %43 = load %struct.Node*, %struct.Node** %42, align 8
  %44 = load i32, i32* %5, align 4
  %45 = call %struct.Node* @add(%struct.Node* noundef %43, i32 noundef %44)
  %46 = load %struct.Node*, %struct.Node** %4, align 8
  %47 = getelementptr inbounds %struct.Node, %struct.Node* %46, i32 0, i32 4
  store %struct.Node* %45, %struct.Node** %47, align 8
  br label %53

48:                                               ; preds = %34
  %49 = load %struct.Node*, %struct.Node** %4, align 8
  %50 = getelementptr inbounds %struct.Node, %struct.Node* %49, i32 0, i32 2
  %51 = load i32, i32* %50, align 8
  %52 = add nsw i32 %51, 1
  store i32 %52, i32* %50, align 8
  br label %53

53:                                               ; preds = %48, %40
  br label %54

54:                                               ; preds = %53, %26
  %55 = load %struct.Node*, %struct.Node** %4, align 8
  %56 = call %struct.Node* @rebalance(%struct.Node* noundef %55)
  store %struct.Node* %56, %struct.Node** %3, align 8
  br label %57

57:                                               ; preds = %54, %9
  %58 = load %struct.Node*, %struct.Node** %3, align 8
  ret %struct.Node* %58
}

; Function Attrs: nounwind
declare noalias i8* @calloc(i64 noundef, i64 noundef) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local void @in_order_traversal(%struct.Node* noundef %0, %struct.my_arr* noundef %1) #0 {
  %3 = alloca %struct.Node*, align 8
  %4 = alloca %struct.my_arr*, align 8
  %5 = alloca i32, align 4
  store %struct.Node* %0, %struct.Node** %3, align 8
  store %struct.my_arr* %1, %struct.my_arr** %4, align 8
  %6 = load %struct.Node*, %struct.Node** %3, align 8
  %7 = icmp ne %struct.Node* %6, null
  br i1 %7, label %8, label %34

8:                                                ; preds = %2
  %9 = load %struct.Node*, %struct.Node** %3, align 8
  %10 = getelementptr inbounds %struct.Node, %struct.Node* %9, i32 0, i32 3
  %11 = load %struct.Node*, %struct.Node** %10, align 8
  %12 = load %struct.my_arr*, %struct.my_arr** %4, align 8
  call void @in_order_traversal(%struct.Node* noundef %11, %struct.my_arr* noundef %12)
  store i32 0, i32* %5, align 4
  br label %13

13:                                               ; preds = %24, %8
  %14 = load i32, i32* %5, align 4
  %15 = load %struct.Node*, %struct.Node** %3, align 8
  %16 = getelementptr inbounds %struct.Node, %struct.Node* %15, i32 0, i32 2
  %17 = load i32, i32* %16, align 8
  %18 = icmp slt i32 %14, %17
  br i1 %18, label %19, label %27

19:                                               ; preds = %13
  %20 = load %struct.my_arr*, %struct.my_arr** %4, align 8
  %21 = load %struct.Node*, %struct.Node** %3, align 8
  %22 = getelementptr inbounds %struct.Node, %struct.Node* %21, i32 0, i32 0
  %23 = load i32, i32* %22, align 8
  call void @add_my_arr(%struct.my_arr* noundef %20, i32 noundef %23)
  br label %24

24:                                               ; preds = %19
  %25 = load i32, i32* %5, align 4
  %26 = add nsw i32 %25, 1
  store i32 %26, i32* %5, align 4
  br label %13, !llvm.loop !8

27:                                               ; preds = %13
  %28 = load %struct.Node*, %struct.Node** %3, align 8
  %29 = getelementptr inbounds %struct.Node, %struct.Node* %28, i32 0, i32 4
  %30 = load %struct.Node*, %struct.Node** %29, align 8
  %31 = load %struct.my_arr*, %struct.my_arr** %4, align 8
  call void @in_order_traversal(%struct.Node* noundef %30, %struct.my_arr* noundef %31)
  %32 = load %struct.Node*, %struct.Node** %3, align 8
  %33 = bitcast %struct.Node* %32 to i8*
  call void @free(i8* noundef %33) #4
  br label %34

34:                                               ; preds = %27, %2
  ret void
}

; Function Attrs: nounwind
declare void @free(i8* noundef) #1

; Function Attrs: noinline nounwind optnone uwtable
define dso_local i32 @main() #0 {
  %1 = alloca i32, align 4
  %2 = alloca %struct._IO_FILE*, align 8
  %3 = alloca %struct._IO_FILE*, align 8
  %4 = alloca i32, align 4
  %5 = alloca %struct.my_arr, align 8
  %6 = alloca %struct.Node*, align 8
  %7 = alloca i32, align 4
  store i32 0, i32* %1, align 4
  %8 = call noalias %struct._IO_FILE* @fopen(i8* noundef getelementptr inbounds ([10 x i8], [10 x i8]* @.str, i64 0, i64 0), i8* noundef getelementptr inbounds ([3 x i8], [3 x i8]* @.str.1, i64 0, i64 0))
  store %struct._IO_FILE* %8, %struct._IO_FILE** %2, align 8
  %9 = call noalias %struct._IO_FILE* @fopen(i8* noundef getelementptr inbounds ([11 x i8], [11 x i8]* @.str.2, i64 0, i64 0), i8* noundef getelementptr inbounds ([3 x i8], [3 x i8]* @.str.3, i64 0, i64 0))
  store %struct._IO_FILE* %9, %struct._IO_FILE** %3, align 8
  %10 = bitcast i32* %4 to i8*
  %11 = load %struct._IO_FILE*, %struct._IO_FILE** %2, align 8
  %12 = call i64 @fread(i8* noundef %10, i64 noundef 4, i64 noundef 1, %struct._IO_FILE* noundef %11)
  %13 = load i32, i32* %4, align 4
  %14 = sext i32 %13 to i64
  %15 = call noalias i8* @calloc(i64 noundef %14, i64 noundef 4) #4
  %16 = bitcast i8* %15 to i32*
  %17 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 0
  store i32* %16, i32** %17, align 8
  %18 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 0
  %19 = load i32*, i32** %18, align 8
  %20 = bitcast i32* %19 to i8*
  %21 = load i32, i32* %4, align 4
  %22 = sext i32 %21 to i64
  %23 = load %struct._IO_FILE*, %struct._IO_FILE** %2, align 8
  %24 = call i64 @fread(i8* noundef %20, i64 noundef 4, i64 noundef %22, %struct._IO_FILE* noundef %23)
  %25 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 0
  %26 = load i32*, i32** %25, align 8
  %27 = load i32, i32* %4, align 4
  call void @shuffle(i32* noundef %26, i32 noundef %27)
  store %struct.Node* null, %struct.Node** %6, align 8
  store i32 0, i32* %7, align 4
  br label %28

28:                                               ; preds = %41, %0
  %29 = load i32, i32* %7, align 4
  %30 = load i32, i32* %4, align 4
  %31 = icmp slt i32 %29, %30
  br i1 %31, label %32, label %44

32:                                               ; preds = %28
  %33 = load %struct.Node*, %struct.Node** %6, align 8
  %34 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 0
  %35 = load i32*, i32** %34, align 8
  %36 = load i32, i32* %7, align 4
  %37 = sext i32 %36 to i64
  %38 = getelementptr inbounds i32, i32* %35, i64 %37
  %39 = load i32, i32* %38, align 4
  %40 = call %struct.Node* @add(%struct.Node* noundef %33, i32 noundef %39)
  store %struct.Node* %40, %struct.Node** %6, align 8
  br label %41

41:                                               ; preds = %32
  %42 = load i32, i32* %7, align 4
  %43 = add nsw i32 %42, 1
  store i32 %43, i32* %7, align 4
  br label %28, !llvm.loop !9

44:                                               ; preds = %28
  %45 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 1
  store i32 0, i32* %45, align 8
  %46 = load %struct.Node*, %struct.Node** %6, align 8
  call void @in_order_traversal(%struct.Node* noundef %46, %struct.my_arr* noundef %5)
  %47 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 0
  %48 = load i32*, i32** %47, align 8
  %49 = bitcast i32* %48 to i8*
  %50 = load i32, i32* %4, align 4
  %51 = sext i32 %50 to i64
  %52 = load %struct._IO_FILE*, %struct._IO_FILE** %3, align 8
  %53 = call i64 @fwrite(i8* noundef %49, i64 noundef 4, i64 noundef %51, %struct._IO_FILE* noundef %52)
  %54 = getelementptr inbounds %struct.my_arr, %struct.my_arr* %5, i32 0, i32 0
  %55 = load i32*, i32** %54, align 8
  %56 = bitcast i32* %55 to i8*
  call void @free(i8* noundef %56) #4
  %57 = load %struct._IO_FILE*, %struct._IO_FILE** %2, align 8
  %58 = call i32 @fclose(%struct._IO_FILE* noundef %57)
  %59 = load %struct._IO_FILE*, %struct._IO_FILE** %3, align 8
  %60 = call i32 @fclose(%struct._IO_FILE* noundef %59)
  ret i32 0
}

declare noalias %struct._IO_FILE* @fopen(i8* noundef, i8* noundef) #3

declare i64 @fread(i8* noundef, i64 noundef, i64 noundef, %struct._IO_FILE* noundef) #3

declare i64 @fwrite(i8* noundef, i64 noundef, i64 noundef, %struct._IO_FILE* noundef) #3

declare i32 @fclose(%struct._IO_FILE* noundef) #3

attributes #0 = { noinline nounwind optnone uwtable "frame-pointer"="all" "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { nounwind "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { nounwind readnone willreturn "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #3 = { "frame-pointer"="all" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #4 = { nounwind }
attributes #5 = { nounwind readnone willreturn }

!llvm.module.flags = !{!0, !1, !2, !3, !4}
!llvm.ident = !{!5}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 7, !"PIC Level", i32 2}
!2 = !{i32 7, !"PIE Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 1}
!4 = !{i32 7, !"frame-pointer", i32 2}
!5 = !{!"Ubuntu clang version 14.0.0-1ubuntu1.1"}
!6 = distinct !{!6, !7}
!7 = !{!"llvm.loop.mustprogress"}
!8 = distinct !{!8, !7}
!9 = distinct !{!9, !7}
