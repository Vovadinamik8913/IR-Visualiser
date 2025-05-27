; ModuleID = 'avias.c'
source_filename = "avias.c"
target datalayout = "e-m:e-p270:32:32-p271:32:32-p272:64:64-i64:64-i128:128-f80:128-n8:16:32:64-S128"
target triple = "x86_64-pc-linux-gnu"

%struct.vector = type { ptr, i32, i32 }
%struct.Edge = type { i32, i64, i32 }
%struct.pair = type { i32, i32 }
%struct.Heap = type { [100001 x i32], i32 }

@ind = dso_local local_unnamed_addr global i32 1, align 4
@dist1 = dso_local local_unnamed_addr global [100001 x i64] zeroinitializer, align 16
@num = dso_local local_unnamed_addr global [100001 x i32] zeroinitializer, align 16
@N = dso_local global i32 0, align 4
@verticals = dso_local local_unnamed_addr global [100001 x %struct.vector] zeroinitializer, align 16
@F = dso_local local_unnamed_addr global [100001 x %struct.Edge] zeroinitializer, align 16
@.str = private unnamed_addr constant [9 x i8] c"%d %d %d\00", align 1
@M = dso_local global i32 0, align 4
@K = dso_local global i32 0, align 4
@.str.1 = private unnamed_addr constant [6 x i8] c"%d %d\00", align 1
@request = dso_local local_unnamed_addr global [21 x %struct.pair] zeroinitializer, align 16
@.str.2 = private unnamed_addr constant [11 x i8] c"%d %d %llu\00", align 1
@.str.3 = private unnamed_addr constant [16 x i8] c"quarantine %lu \00", align 1
@.str.4 = private unnamed_addr constant [4 x i8] c"%d \00", align 1
@str = private unnamed_addr constant [7 x i8] c"DOOMED\00", align 1

; Function Attrs: mustprogress nounwind willreturn uwtable
define dso_local void @resize(ptr nocapture noundef %0) local_unnamed_addr #0 {
  %2 = getelementptr inbounds %struct.vector, ptr %0, i64 0, i32 2
  %3 = load i32, ptr %2, align 4, !tbaa !5
  %4 = icmp eq i32 %3, 0
  br i1 %4, label %5, label %7

5:                                                ; preds = %1
  %6 = tail call noalias dereferenceable_or_null(24) ptr @malloc(i64 noundef 24) #10
  store ptr %6, ptr %0, align 8, !tbaa !11
  store i32 1, ptr %2, align 4, !tbaa !5
  br label %13

7:                                                ; preds = %1
  %8 = shl nsw i32 %3, 1
  store i32 %8, ptr %2, align 4, !tbaa !5
  %9 = load ptr, ptr %0, align 8, !tbaa !11
  %10 = sext i32 %8 to i64
  %11 = mul nsw i64 %10, 24
  %12 = tail call ptr @realloc(ptr noundef %9, i64 noundef %11) #11
  store ptr %12, ptr %0, align 8, !tbaa !11
  br label %13

13:                                               ; preds = %7, %5
  ret void
}

; Function Attrs: mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite)
declare noalias noundef ptr @malloc(i64 noundef) local_unnamed_addr #1

; Function Attrs: mustprogress nounwind willreturn allockind("realloc") allocsize(1) memory(argmem: readwrite, inaccessiblemem: readwrite)
declare noalias noundef ptr @realloc(ptr allocptr nocapture noundef, i64 noundef) local_unnamed_addr #2

; Function Attrs: mustprogress nounwind willreturn uwtable
define dso_local void @add(ptr nocapture noundef %0, i32 noundef %1, i64 noundef %2) local_unnamed_addr #0 {
  %4 = getelementptr inbounds %struct.vector, ptr %0, i64 0, i32 2
  %5 = load i32, ptr %4, align 4, !tbaa !5
  %6 = getelementptr inbounds %struct.vector, ptr %0, i64 0, i32 1
  %7 = load i32, ptr %6, align 8, !tbaa !12
  %8 = add nsw i32 %7, 1
  %9 = icmp sgt i32 %5, %8
  br i1 %9, label %20, label %10

10:                                               ; preds = %3
  %11 = icmp eq i32 %5, 0
  br i1 %11, label %12, label %14

12:                                               ; preds = %10
  %13 = tail call noalias dereferenceable_or_null(24) ptr @malloc(i64 noundef 24) #10
  store ptr %13, ptr %0, align 8, !tbaa !11
  store i32 1, ptr %4, align 4, !tbaa !5
  br label %20

14:                                               ; preds = %10
  %15 = shl nsw i32 %5, 1
  store i32 %15, ptr %4, align 4, !tbaa !5
  %16 = load ptr, ptr %0, align 8, !tbaa !11
  %17 = sext i32 %15 to i64
  %18 = mul nsw i64 %17, 24
  %19 = tail call ptr @realloc(ptr noundef %16, i64 noundef %18) #11
  store ptr %19, ptr %0, align 8, !tbaa !11
  br label %20

20:                                               ; preds = %14, %12, %3
  %21 = load ptr, ptr %0, align 8, !tbaa !11
  %22 = load i32, ptr %6, align 8, !tbaa !12
  %23 = sext i32 %22 to i64
  %24 = getelementptr inbounds %struct.Edge, ptr %21, i64 %23
  store i32 %1, ptr %24, align 8, !tbaa !13
  %25 = load i32, ptr @ind, align 4, !tbaa !16
  %26 = getelementptr inbounds %struct.Edge, ptr %21, i64 %23, i32 2
  store i32 %25, ptr %26, align 8, !tbaa !17
  %27 = add nsw i32 %22, 1
  store i32 %27, ptr %6, align 8, !tbaa !12
  %28 = getelementptr inbounds %struct.Edge, ptr %21, i64 %23, i32 1
  store i64 %2, ptr %28, align 8, !tbaa !18
  ret void
}

; Function Attrs: mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: readwrite) uwtable
define dso_local void @swap(ptr nocapture noundef %0, ptr nocapture noundef %1) local_unnamed_addr #3 {
  %3 = load i32, ptr %0, align 4, !tbaa !16
  %4 = load i32, ptr %1, align 4, !tbaa !16
  store i32 %4, ptr %0, align 4, !tbaa !16
  store i32 %3, ptr %1, align 4, !tbaa !16
  ret void
}

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite)
declare void @llvm.lifetime.start.p0(i64 immarg, ptr nocapture) #4

; Function Attrs: mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite)
declare void @llvm.lifetime.end.p0(i64 immarg, ptr nocapture) #4

; Function Attrs: nofree nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @SiftDown(ptr nocapture noundef %0, i32 noundef %1) local_unnamed_addr #5 {
  br label %3

3:                                                ; preds = %47, %2
  %4 = phi i32 [ %1, %2 ], [ %45, %47 ]
  %5 = shl nsw i32 %4, 1
  %6 = or disjoint i32 %5, 1
  %7 = add nsw i32 %5, 2
  %8 = getelementptr inbounds %struct.Heap, ptr %0, i64 0, i32 1
  %9 = load i32, ptr %8, align 4, !tbaa !19
  %10 = icmp slt i32 %6, %9
  br i1 %10, label %11, label %26

11:                                               ; preds = %3
  %12 = sext i32 %6 to i64
  %13 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %12
  %14 = load i32, ptr %13, align 4, !tbaa !16
  %15 = sext i32 %14 to i64
  %16 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %15
  %17 = load i64, ptr %16, align 8, !tbaa !21
  %18 = sext i32 %4 to i64
  %19 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %18
  %20 = load i32, ptr %19, align 4, !tbaa !16
  %21 = sext i32 %20 to i64
  %22 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %21
  %23 = load i64, ptr %22, align 8, !tbaa !21
  %24 = icmp ult i64 %17, %23
  %25 = select i1 %24, i32 %6, i32 %4
  br label %26

26:                                               ; preds = %11, %3
  %27 = phi i32 [ %4, %3 ], [ %25, %11 ]
  %28 = icmp slt i32 %7, %9
  br i1 %28, label %29, label %44

29:                                               ; preds = %26
  %30 = sext i32 %7 to i64
  %31 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %30
  %32 = load i32, ptr %31, align 4, !tbaa !16
  %33 = sext i32 %32 to i64
  %34 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %33
  %35 = load i64, ptr %34, align 8, !tbaa !21
  %36 = sext i32 %27 to i64
  %37 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %36
  %38 = load i32, ptr %37, align 4, !tbaa !16
  %39 = sext i32 %38 to i64
  %40 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %39
  %41 = load i64, ptr %40, align 8, !tbaa !21
  %42 = icmp ult i64 %35, %41
  %43 = select i1 %42, i32 %7, i32 %27
  br label %44

44:                                               ; preds = %29, %26
  %45 = phi i32 [ %27, %26 ], [ %43, %29 ]
  %46 = icmp eq i32 %45, %4
  br i1 %46, label %60, label %47

47:                                               ; preds = %44
  %48 = sext i32 %4 to i64
  %49 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %48
  %50 = sext i32 %45 to i64
  %51 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %50
  %52 = load i32, ptr %49, align 4, !tbaa !16
  %53 = load i32, ptr %51, align 4, !tbaa !16
  store i32 %53, ptr %49, align 4, !tbaa !16
  store i32 %52, ptr %51, align 4, !tbaa !16
  %54 = load i32, ptr %49, align 4, !tbaa !16
  %55 = sext i32 %54 to i64
  %56 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %55
  store i32 %4, ptr %56, align 4, !tbaa !16
  %57 = load i32, ptr %51, align 4, !tbaa !16
  %58 = sext i32 %57 to i64
  %59 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %58
  store i32 %45, ptr %59, align 4, !tbaa !16
  br label %3

60:                                               ; preds = %44
  ret void
}

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @SiftUp(ptr nocapture noundef %0, i32 noundef %1) local_unnamed_addr #6 {
  %3 = icmp sgt i32 %1, 0
  br i1 %3, label %4, label %29

4:                                                ; preds = %2, %21
  %5 = phi i32 [ %13, %21 ], [ %1, %2 ]
  %6 = zext nneg i32 %5 to i64
  %7 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %6
  %8 = load i32, ptr %7, align 4, !tbaa !16
  %9 = sext i32 %8 to i64
  %10 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %9
  %11 = load i64, ptr %10, align 8, !tbaa !21
  %12 = add nsw i32 %5, -1
  %13 = lshr i32 %12, 1
  %14 = zext nneg i32 %13 to i64
  %15 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %14
  %16 = load i32, ptr %15, align 4, !tbaa !16
  %17 = sext i32 %16 to i64
  %18 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %17
  %19 = load i64, ptr %18, align 8, !tbaa !21
  %20 = icmp ult i64 %11, %19
  br i1 %20, label %21, label %29

21:                                               ; preds = %4
  %22 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %9
  store i32 %13, ptr %22, align 4, !tbaa !16
  %23 = load i32, ptr %15, align 4, !tbaa !16
  %24 = sext i32 %23 to i64
  %25 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %24
  store i32 %5, ptr %25, align 4, !tbaa !16
  %26 = load i32, ptr %7, align 4, !tbaa !16
  %27 = load i32, ptr %15, align 4, !tbaa !16
  store i32 %27, ptr %7, align 4, !tbaa !16
  store i32 %26, ptr %15, align 4, !tbaa !16
  %28 = icmp ult i32 %12, 2
  br i1 %28, label %29, label %4, !llvm.loop !22

29:                                               ; preds = %4, %21, %2
  ret void
}

; Function Attrs: nofree nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local i32 @pop(ptr nocapture noundef %0) local_unnamed_addr #5 {
  %2 = load i32, ptr %0, align 4, !tbaa !16
  %3 = getelementptr inbounds %struct.Heap, ptr %0, i64 0, i32 1
  %4 = load i32, ptr %3, align 4, !tbaa !19
  %5 = add nsw i32 %4, -1
  store i32 %5, ptr %3, align 4, !tbaa !19
  %6 = sext i32 %5 to i64
  %7 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %6
  %8 = load i32, ptr %7, align 4, !tbaa !16
  store i32 %8, ptr %0, align 4, !tbaa !16
  %9 = sext i32 %8 to i64
  %10 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %9
  store i32 0, ptr %10, align 4, !tbaa !16
  tail call void @SiftDown(ptr noundef nonnull %0, i32 noundef 0)
  ret i32 %2
}

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @push(ptr nocapture noundef %0, i32 noundef %1) local_unnamed_addr #6 {
  %3 = getelementptr inbounds %struct.Heap, ptr %0, i64 0, i32 1
  %4 = load i32, ptr %3, align 4, !tbaa !19
  %5 = sext i32 %1 to i64
  %6 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %5
  store i32 %4, ptr %6, align 4, !tbaa !16
  %7 = load i32, ptr %3, align 4, !tbaa !19
  %8 = add nsw i32 %7, 1
  store i32 %8, ptr %3, align 4, !tbaa !19
  %9 = sext i32 %7 to i64
  %10 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %9
  store i32 %1, ptr %10, align 4, !tbaa !16
  %11 = load i32, ptr %3, align 4, !tbaa !19
  %12 = icmp sgt i32 %11, 1
  br i1 %12, label %13, label %40

13:                                               ; preds = %2
  %14 = add nsw i32 %11, -1
  br label %15

15:                                               ; preds = %13, %32
  %16 = phi i32 [ %24, %32 ], [ %14, %13 ]
  %17 = zext nneg i32 %16 to i64
  %18 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %17
  %19 = load i32, ptr %18, align 4, !tbaa !16
  %20 = sext i32 %19 to i64
  %21 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %20
  %22 = load i64, ptr %21, align 8, !tbaa !21
  %23 = add nsw i32 %16, -1
  %24 = lshr i32 %23, 1
  %25 = zext nneg i32 %24 to i64
  %26 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %25
  %27 = load i32, ptr %26, align 4, !tbaa !16
  %28 = sext i32 %27 to i64
  %29 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %28
  %30 = load i64, ptr %29, align 8, !tbaa !21
  %31 = icmp ult i64 %22, %30
  br i1 %31, label %32, label %40

32:                                               ; preds = %15
  %33 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %20
  store i32 %24, ptr %33, align 4, !tbaa !16
  %34 = load i32, ptr %26, align 4, !tbaa !16
  %35 = sext i32 %34 to i64
  %36 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %35
  store i32 %16, ptr %36, align 4, !tbaa !16
  %37 = load i32, ptr %18, align 4, !tbaa !16
  %38 = load i32, ptr %26, align 4, !tbaa !16
  store i32 %38, ptr %18, align 4, !tbaa !16
  store i32 %37, ptr %26, align 4, !tbaa !16
  %39 = icmp ult i32 %23, 2
  br i1 %39, label %40, label %15, !llvm.loop !22

40:                                               ; preds = %15, %32, %2
  ret void
}

; Function Attrs: nofree nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @dijkstra(i32 noundef %0, ptr nocapture noundef %1) local_unnamed_addr #5 {
  %3 = load i32, ptr @N, align 4, !tbaa !16
  %4 = icmp slt i32 %3, 1
  br i1 %4, label %8, label %5

5:                                                ; preds = %2
  %6 = add nuw i32 %3, 1
  %7 = zext i32 %6 to i64
  br label %15

8:                                                ; preds = %15, %2
  %9 = sext i32 %0 to i64
  %10 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %9
  store i64 0, ptr %10, align 8, !tbaa !21
  %11 = load i32, ptr @N, align 4, !tbaa !16
  %12 = icmp slt i32 %11, 1
  br i1 %12, label %20, label %13

13:                                               ; preds = %8
  %14 = getelementptr inbounds %struct.Heap, ptr %1, i64 0, i32 1
  br label %24

15:                                               ; preds = %5, %15
  %16 = phi i64 [ 1, %5 ], [ %18, %15 ]
  %17 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %16
  store i64 60000000001, ptr %17, align 8, !tbaa !21
  %18 = add nuw nsw i64 %16, 1
  %19 = icmp eq i64 %18, %7
  br i1 %19, label %8, label %15, !llvm.loop !25

20:                                               ; preds = %62, %8
  %21 = getelementptr inbounds %struct.Heap, ptr %1, i64 0, i32 1
  %22 = load i32, ptr %21, align 4, !tbaa !19
  %23 = icmp eq i32 %22, 0
  br i1 %23, label %139, label %70

24:                                               ; preds = %13, %62
  %25 = phi i64 [ 1, %13 ], [ %63, %62 ]
  %26 = load i32, ptr %14, align 4, !tbaa !19
  %27 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %25
  store i32 %26, ptr %27, align 4, !tbaa !16
  %28 = load i32, ptr %14, align 4, !tbaa !19
  %29 = add nsw i32 %28, 1
  store i32 %29, ptr %14, align 4, !tbaa !19
  %30 = sext i32 %28 to i64
  %31 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %30
  %32 = trunc i64 %25 to i32
  store i32 %32, ptr %31, align 4, !tbaa !16
  %33 = load i32, ptr %14, align 4, !tbaa !19
  %34 = icmp sgt i32 %33, 1
  br i1 %34, label %35, label %62

35:                                               ; preds = %24
  %36 = add nsw i32 %33, -1
  br label %37

37:                                               ; preds = %54, %35
  %38 = phi i32 [ %46, %54 ], [ %36, %35 ]
  %39 = zext nneg i32 %38 to i64
  %40 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %39
  %41 = load i32, ptr %40, align 4, !tbaa !16
  %42 = sext i32 %41 to i64
  %43 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %42
  %44 = load i64, ptr %43, align 8, !tbaa !21
  %45 = add nsw i32 %38, -1
  %46 = lshr i32 %45, 1
  %47 = zext nneg i32 %46 to i64
  %48 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %47
  %49 = load i32, ptr %48, align 4, !tbaa !16
  %50 = sext i32 %49 to i64
  %51 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %50
  %52 = load i64, ptr %51, align 8, !tbaa !21
  %53 = icmp ult i64 %44, %52
  br i1 %53, label %54, label %62

54:                                               ; preds = %37
  %55 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %42
  store i32 %46, ptr %55, align 4, !tbaa !16
  %56 = load i32, ptr %48, align 4, !tbaa !16
  %57 = sext i32 %56 to i64
  %58 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %57
  store i32 %38, ptr %58, align 4, !tbaa !16
  %59 = load i32, ptr %40, align 4, !tbaa !16
  %60 = load i32, ptr %48, align 4, !tbaa !16
  store i32 %60, ptr %40, align 4, !tbaa !16
  store i32 %59, ptr %48, align 4, !tbaa !16
  %61 = icmp ult i32 %45, 2
  br i1 %61, label %62, label %37, !llvm.loop !22

62:                                               ; preds = %37, %54, %24
  %63 = add nuw nsw i64 %25, 1
  %64 = load i32, ptr @N, align 4, !tbaa !16
  %65 = sext i32 %64 to i64
  %66 = icmp slt i64 %25, %65
  br i1 %66, label %24, label %20, !llvm.loop !26

67:                                               ; preds = %134, %70
  %68 = load i32, ptr %21, align 4, !tbaa !19
  %69 = icmp eq i32 %68, 0
  br i1 %69, label %139, label %70, !llvm.loop !27

70:                                               ; preds = %20, %67
  %71 = phi i32 [ %68, %67 ], [ %22, %20 ]
  %72 = load i32, ptr %1, align 4, !tbaa !16
  %73 = add nsw i32 %71, -1
  store i32 %73, ptr %21, align 4, !tbaa !19
  %74 = sext i32 %73 to i64
  %75 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %74
  %76 = load i32, ptr %75, align 4, !tbaa !16
  store i32 %76, ptr %1, align 4, !tbaa !16
  %77 = sext i32 %76 to i64
  %78 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %77
  store i32 0, ptr %78, align 4, !tbaa !16
  tail call void @SiftDown(ptr noundef nonnull %1, i32 noundef 0)
  %79 = sext i32 %72 to i64
  %80 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %79, i32 1
  %81 = load i32, ptr %80, align 8, !tbaa !12
  %82 = icmp sgt i32 %81, 0
  br i1 %82, label %83, label %67

83:                                               ; preds = %70
  %84 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %79
  %85 = load ptr, ptr %84, align 16, !tbaa !11
  %86 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %79
  br label %87

87:                                               ; preds = %83, %134
  %88 = phi i64 [ 0, %83 ], [ %135, %134 ]
  %89 = getelementptr inbounds %struct.Edge, ptr %85, i64 %88
  %90 = load i32, ptr %89, align 8, !tbaa !13
  %91 = sext i32 %90 to i64
  %92 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %91
  %93 = load i64, ptr %92, align 8, !tbaa !21
  %94 = load i64, ptr %86, align 8, !tbaa !21
  %95 = getelementptr inbounds %struct.Edge, ptr %85, i64 %88, i32 1
  %96 = load i64, ptr %95, align 8, !tbaa !18
  %97 = add i64 %96, %94
  %98 = icmp ugt i64 %93, %97
  br i1 %98, label %99, label %134

99:                                               ; preds = %87
  store i64 %97, ptr %92, align 8, !tbaa !21
  %100 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %91
  store i32 %72, ptr %100, align 8, !tbaa !13
  %101 = getelementptr inbounds %struct.Edge, ptr %85, i64 %88, i32 2
  %102 = load i32, ptr %101, align 8, !tbaa !17
  %103 = load i32, ptr %89, align 8, !tbaa !13
  %104 = sext i32 %103 to i64
  %105 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %104, i32 2
  store i32 %102, ptr %105, align 8, !tbaa !17
  %106 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %104
  %107 = load i32, ptr %106, align 4, !tbaa !16
  %108 = icmp sgt i32 %107, 0
  br i1 %108, label %109, label %134

109:                                              ; preds = %99, %126
  %110 = phi i32 [ %118, %126 ], [ %107, %99 ]
  %111 = zext nneg i32 %110 to i64
  %112 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %111
  %113 = load i32, ptr %112, align 4, !tbaa !16
  %114 = sext i32 %113 to i64
  %115 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %114
  %116 = load i64, ptr %115, align 8, !tbaa !21
  %117 = add nsw i32 %110, -1
  %118 = lshr i32 %117, 1
  %119 = zext nneg i32 %118 to i64
  %120 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %119
  %121 = load i32, ptr %120, align 4, !tbaa !16
  %122 = sext i32 %121 to i64
  %123 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %122
  %124 = load i64, ptr %123, align 8, !tbaa !21
  %125 = icmp ult i64 %116, %124
  br i1 %125, label %126, label %134

126:                                              ; preds = %109
  %127 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %114
  store i32 %118, ptr %127, align 4, !tbaa !16
  %128 = load i32, ptr %120, align 4, !tbaa !16
  %129 = sext i32 %128 to i64
  %130 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %129
  store i32 %110, ptr %130, align 4, !tbaa !16
  %131 = load i32, ptr %112, align 4, !tbaa !16
  %132 = load i32, ptr %120, align 4, !tbaa !16
  store i32 %132, ptr %112, align 4, !tbaa !16
  store i32 %131, ptr %120, align 4, !tbaa !16
  %133 = icmp ult i32 %117, 2
  br i1 %133, label %134, label %109, !llvm.loop !22

134:                                              ; preds = %126, %109, %99, %87
  %135 = add nuw nsw i64 %88, 1
  %136 = load i32, ptr %80, align 8, !tbaa !12
  %137 = sext i32 %136 to i64
  %138 = icmp slt i64 %135, %137
  br i1 %138, label %87, label %67, !llvm.loop !28

139:                                              ; preds = %67, %20
  ret void
}

; Function Attrs: nounwind uwtable
define dso_local noundef i32 @main() local_unnamed_addr #7 {
  %1 = alloca i32, align 4
  %2 = alloca i32, align 4
  %3 = alloca i32, align 4
  %4 = alloca i32, align 4
  %5 = alloca i64, align 8
  %6 = alloca %struct.Heap, align 4
  %7 = alloca [100001 x i32], align 16
  %8 = tail call i32 (ptr, ...) @__isoc99_scanf(ptr noundef nonnull @.str, ptr noundef nonnull @N, ptr noundef nonnull @M, ptr noundef nonnull @K)
  %9 = load i32, ptr @K, align 4, !tbaa !16
  %10 = icmp sgt i32 %9, 0
  br i1 %10, label %14, label %11

11:                                               ; preds = %14, %0
  %12 = load i32, ptr @M, align 4, !tbaa !16
  %13 = icmp sgt i32 %12, 0
  br i1 %13, label %30, label %25

14:                                               ; preds = %0, %14
  %15 = phi i64 [ %21, %14 ], [ 0, %0 ]
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %1) #12
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %2) #12
  %16 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef nonnull @.str.1, ptr noundef nonnull %1, ptr noundef nonnull %2)
  %17 = load i32, ptr %1, align 4, !tbaa !16
  %18 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %15
  store i32 %17, ptr %18, align 8, !tbaa !29
  %19 = load i32, ptr %2, align 4, !tbaa !16
  %20 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %15, i32 1
  store i32 %19, ptr %20, align 4, !tbaa !31
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %2) #12
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %1) #12
  %21 = add nuw nsw i64 %15, 1
  %22 = load i32, ptr @K, align 4, !tbaa !16
  %23 = sext i32 %22 to i64
  %24 = icmp slt i64 %21, %23
  br i1 %24, label %14, label %11, !llvm.loop !32

25:                                               ; preds = %54, %11
  %26 = load i32, ptr @K, align 4, !tbaa !16
  %27 = icmp sgt i32 %26, 0
  br i1 %27, label %28, label %68

28:                                               ; preds = %25
  %29 = getelementptr inbounds %struct.Heap, ptr %6, i64 0, i32 1
  br label %69

30:                                               ; preds = %11, %54
  %31 = phi i32 [ %65, %54 ], [ 0, %11 ]
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %3) #12
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %4) #12
  call void @llvm.lifetime.start.p0(i64 8, ptr nonnull %5) #12
  %32 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef nonnull @.str.2, ptr noundef nonnull %3, ptr noundef nonnull %4, ptr noundef nonnull %5)
  %33 = load i32, ptr %3, align 4, !tbaa !16
  %34 = sext i32 %33 to i64
  %35 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %34
  %36 = load i32, ptr %4, align 4, !tbaa !16
  %37 = load i64, ptr %5, align 8, !tbaa !33
  %38 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %34, i32 2
  %39 = load i32, ptr %38, align 4, !tbaa !5
  %40 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %34, i32 1
  %41 = load i32, ptr %40, align 8, !tbaa !12
  %42 = add nsw i32 %41, 1
  %43 = icmp sgt i32 %39, %42
  br i1 %43, label %54, label %44

44:                                               ; preds = %30
  %45 = icmp eq i32 %39, 0
  br i1 %45, label %46, label %48

46:                                               ; preds = %44
  %47 = call noalias dereferenceable_or_null(24) ptr @malloc(i64 noundef 24) #10
  store ptr %47, ptr %35, align 16, !tbaa !11
  store i32 1, ptr %38, align 4, !tbaa !5
  br label %54

48:                                               ; preds = %44
  %49 = shl nsw i32 %39, 1
  store i32 %49, ptr %38, align 4, !tbaa !5
  %50 = load ptr, ptr %35, align 16, !tbaa !11
  %51 = sext i32 %49 to i64
  %52 = mul nsw i64 %51, 24
  %53 = call ptr @realloc(ptr noundef %50, i64 noundef %52) #11
  store ptr %53, ptr %35, align 16, !tbaa !11
  br label %54

54:                                               ; preds = %30, %46, %48
  %55 = load ptr, ptr %35, align 16, !tbaa !11
  %56 = load i32, ptr %40, align 8, !tbaa !12
  %57 = sext i32 %56 to i64
  %58 = getelementptr inbounds %struct.Edge, ptr %55, i64 %57
  store i32 %36, ptr %58, align 8, !tbaa !13
  %59 = load i32, ptr @ind, align 4, !tbaa !16
  %60 = getelementptr inbounds %struct.Edge, ptr %55, i64 %57, i32 2
  store i32 %59, ptr %60, align 8, !tbaa !17
  %61 = add nsw i32 %56, 1
  store i32 %61, ptr %40, align 8, !tbaa !12
  %62 = getelementptr inbounds %struct.Edge, ptr %55, i64 %57, i32 1
  store i64 %37, ptr %62, align 8, !tbaa !18
  %63 = load i32, ptr @ind, align 4, !tbaa !16
  %64 = add nsw i32 %63, 1
  store i32 %64, ptr @ind, align 4, !tbaa !16
  call void @llvm.lifetime.end.p0(i64 8, ptr nonnull %5) #12
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %4) #12
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %3) #12
  %65 = add nuw nsw i32 %31, 1
  %66 = load i32, ptr @M, align 4, !tbaa !16
  %67 = icmp slt i32 %65, %66
  br i1 %67, label %30, label %25, !llvm.loop !35

68:                                               ; preds = %114, %25
  ret i32 0

69:                                               ; preds = %28, %114
  %70 = phi i64 [ 0, %28 ], [ %115, %114 ]
  call void @llvm.lifetime.start.p0(i64 400008, ptr nonnull %6) #12
  store i32 0, ptr %29, align 4, !tbaa !19
  %71 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %70
  %72 = load i32, ptr %71, align 8, !tbaa !29
  call void @dijkstra(i32 noundef %72, ptr noundef nonnull %6)
  %73 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %70, i32 1
  %74 = load i32, ptr %73, align 4, !tbaa !31
  %75 = sext i32 %74 to i64
  %76 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %75
  %77 = load i64, ptr %76, align 8, !tbaa !21
  %78 = icmp eq i64 %77, 60000000001
  br i1 %78, label %112, label %79

79:                                               ; preds = %69
  %80 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.3, i64 noundef %77)
  %81 = load i32, ptr %71, align 8, !tbaa !29
  %82 = load i32, ptr %73, align 4, !tbaa !31
  call void @llvm.lifetime.start.p0(i64 400004, ptr nonnull %7) #12
  %83 = icmp eq i32 %81, %82
  br i1 %83, label %97, label %84

84:                                               ; preds = %79, %84
  %85 = phi i64 [ %91, %84 ], [ 0, %79 ]
  %86 = phi i32 [ %93, %84 ], [ %82, %79 ]
  %87 = sext i32 %86 to i64
  %88 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %87
  %89 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %87, i32 2
  %90 = load i32, ptr %89, align 8, !tbaa !17
  %91 = add nuw i64 %85, 1
  %92 = getelementptr inbounds [100001 x i32], ptr %7, i64 0, i64 %85
  store i32 %90, ptr %92, align 4, !tbaa !16
  %93 = load i32, ptr %88, align 8, !tbaa !13
  %94 = icmp eq i32 %81, %93
  br i1 %94, label %95, label %84, !llvm.loop !36

95:                                               ; preds = %84
  %96 = trunc i64 %91 to i32
  br label %97

97:                                               ; preds = %95, %79
  %98 = phi i32 [ 0, %79 ], [ %96, %95 ]
  %99 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.4, i32 noundef %98)
  %100 = icmp sgt i32 %98, 0
  br i1 %100, label %101, label %103

101:                                              ; preds = %97
  %102 = zext nneg i32 %98 to i64
  br label %105

103:                                              ; preds = %105, %97
  %104 = call i32 @putchar(i32 10)
  call void @llvm.lifetime.end.p0(i64 400004, ptr nonnull %7) #12
  br label %114

105:                                              ; preds = %101, %105
  %106 = phi i64 [ %102, %101 ], [ %107, %105 ]
  %107 = add nsw i64 %106, -1
  %108 = getelementptr inbounds [100001 x i32], ptr %7, i64 0, i64 %107
  %109 = load i32, ptr %108, align 4, !tbaa !16
  %110 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.4, i32 noundef %109)
  %111 = icmp sgt i64 %106, 1
  br i1 %111, label %105, label %103, !llvm.loop !37

112:                                              ; preds = %69
  %113 = call i32 @puts(ptr nonnull dereferenceable(1) @str)
  br label %114

114:                                              ; preds = %112, %103
  call void @llvm.lifetime.end.p0(i64 400008, ptr nonnull %6) #12
  %115 = add nuw nsw i64 %70, 1
  %116 = load i32, ptr @K, align 4, !tbaa !16
  %117 = sext i32 %116 to i64
  %118 = icmp slt i64 %115, %117
  br i1 %118, label %69, label %68, !llvm.loop !38
}

; Function Attrs: nofree nounwind
declare noundef i32 @__isoc99_scanf(ptr nocapture noundef readonly, ...) local_unnamed_addr #8

; Function Attrs: nofree nounwind
declare noundef i32 @printf(ptr nocapture noundef readonly, ...) local_unnamed_addr #8

; Function Attrs: nofree nounwind
declare noundef i32 @puts(ptr nocapture noundef readonly) local_unnamed_addr #9

; Function Attrs: nofree nounwind
declare noundef i32 @putchar(i32 noundef) local_unnamed_addr #9

attributes #0 = { mustprogress nounwind willreturn uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite) "alloc-family"="malloc" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { mustprogress nounwind willreturn allockind("realloc") allocsize(1) memory(argmem: readwrite, inaccessiblemem: readwrite) "alloc-family"="malloc" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #3 = { mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: readwrite) uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #4 = { mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite) }
attributes #5 = { nofree nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #6 = { nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #7 = { nounwind uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #8 = { nofree nounwind "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #9 = { nofree nounwind }
attributes #10 = { nounwind allocsize(0) }
attributes #11 = { nounwind allocsize(1) }
attributes #12 = { nounwind }

!llvm.module.flags = !{!0, !1, !2, !3}
!llvm.ident = !{!4}

!0 = !{i32 1, !"wchar_size", i32 4}
!1 = !{i32 8, !"PIC Level", i32 2}
!2 = !{i32 7, !"PIE Level", i32 2}
!3 = !{i32 7, !"uwtable", i32 2}
!4 = !{!"Ubuntu clang version 18.1.8 (++20240731025043+3b5b5c1ec4a3-1~exp1~20240731145144.92)"}
!5 = !{!6, !10, i64 12}
!6 = !{!"vector", !7, i64 0, !10, i64 8, !10, i64 12}
!7 = !{!"any pointer", !8, i64 0}
!8 = !{!"omnipotent char", !9, i64 0}
!9 = !{!"Simple C/C++ TBAA"}
!10 = !{!"int", !8, i64 0}
!11 = !{!6, !7, i64 0}
!12 = !{!6, !10, i64 8}
!13 = !{!14, !10, i64 0}
!14 = !{!"Edge", !10, i64 0, !15, i64 8, !10, i64 16}
!15 = !{!"long", !8, i64 0}
!16 = !{!10, !10, i64 0}
!17 = !{!14, !10, i64 16}
!18 = !{!14, !15, i64 8}
!19 = !{!20, !10, i64 400004}
!20 = !{!"", !8, i64 0, !10, i64 400004}
!21 = !{!15, !15, i64 0}
!22 = distinct !{!22, !23, !24}
!23 = !{!"llvm.loop.mustprogress"}
!24 = !{!"llvm.loop.unroll.disable"}
!25 = distinct !{!25, !23, !24}
!26 = distinct !{!26, !23, !24}
!27 = distinct !{!27, !23, !24}
!28 = distinct !{!28, !23, !24}
!29 = !{!30, !10, i64 0}
!30 = !{!"pair", !10, i64 0, !10, i64 4}
!31 = !{!30, !10, i64 4}
!32 = distinct !{!32, !23, !24}
!33 = !{!34, !34, i64 0}
!34 = !{!"long long", !8, i64 0}
!35 = distinct !{!35, !23, !24}
!36 = distinct !{!36, !23, !24}
!37 = distinct !{!37, !23, !24}
!38 = distinct !{!38, !23, !24}
