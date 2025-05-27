; ModuleID = '../avias.ll'
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
  %2 = getelementptr inbounds i8, ptr %0, i64 12
  %3 = load i32, ptr %2, align 4, !tbaa !5
  %4 = icmp eq i32 %3, 0
  br i1 %4, label %5, label %7

5:                                                ; preds = %1
  %6 = tail call noalias dereferenceable_or_null(24) ptr @malloc(i64 noundef 24) #9
  store i32 1, ptr %2, align 4, !tbaa !5
  br label %13

7:                                                ; preds = %1
  %8 = shl nsw i32 %3, 1
  store i32 %8, ptr %2, align 4, !tbaa !5
  %9 = load ptr, ptr %0, align 8, !tbaa !11
  %10 = sext i32 %8 to i64
  %11 = mul nsw i64 %10, 24
  %12 = tail call ptr @realloc(ptr noundef %9, i64 noundef %11) #10
  br label %13

13:                                               ; preds = %7, %5
  %.sink = phi ptr [ %6, %5 ], [ %12, %7 ]
  store ptr %.sink, ptr %0, align 8
  ret void
}

; Function Attrs: mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite)
declare noalias noundef ptr @malloc(i64 noundef) local_unnamed_addr #1

; Function Attrs: mustprogress nounwind willreturn allockind("realloc") allocsize(1) memory(argmem: readwrite, inaccessiblemem: readwrite)
declare noalias noundef ptr @realloc(ptr allocptr nocapture noundef, i64 noundef) local_unnamed_addr #2

; Function Attrs: mustprogress nounwind willreturn uwtable
define dso_local void @add(ptr nocapture noundef %0, i32 noundef %1, i64 noundef %2) local_unnamed_addr #0 {
  %4 = getelementptr inbounds i8, ptr %0, i64 12
  %5 = load i32, ptr %4, align 4, !tbaa !5
  %6 = getelementptr inbounds i8, ptr %0, i64 8
  %7 = load i32, ptr %6, align 8, !tbaa !12
  %8 = add nsw i32 %7, 1
  %9 = icmp sgt i32 %5, %8
  br i1 %9, label %._crit_edge, label %10

._crit_edge:                                      ; preds = %3
  %.pre = load ptr, ptr %0, align 8, !tbaa !11
  br label %20

10:                                               ; preds = %3
  %11 = icmp eq i32 %5, 0
  br i1 %11, label %12, label %14

12:                                               ; preds = %10
  %13 = tail call noalias dereferenceable_or_null(24) ptr @malloc(i64 noundef 24) #9
  store ptr %13, ptr %0, align 8, !tbaa !11
  store i32 1, ptr %4, align 4, !tbaa !5
  br label %20

14:                                               ; preds = %10
  %15 = shl nsw i32 %5, 1
  store i32 %15, ptr %4, align 4, !tbaa !5
  %16 = load ptr, ptr %0, align 8, !tbaa !11
  %17 = sext i32 %15 to i64
  %18 = mul nsw i64 %17, 24
  %19 = tail call ptr @realloc(ptr noundef %16, i64 noundef %18) #10
  store ptr %19, ptr %0, align 8, !tbaa !11
  %.pre1 = load i32, ptr %6, align 8, !tbaa !12
  %.pre2 = add nsw i32 %.pre1, 1
  br label %20

20:                                               ; preds = %._crit_edge, %14, %12
  %.pre-phi = phi i32 [ %8, %._crit_edge ], [ %.pre2, %14 ], [ %8, %12 ]
  %21 = phi i32 [ %7, %._crit_edge ], [ %.pre1, %14 ], [ %7, %12 ]
  %22 = phi ptr [ %.pre, %._crit_edge ], [ %19, %14 ], [ %13, %12 ]
  %23 = sext i32 %21 to i64
  %24 = getelementptr inbounds %struct.Edge, ptr %22, i64 %23
  store i32 %1, ptr %24, align 8, !tbaa !13
  %25 = load i32, ptr @ind, align 4, !tbaa !16
  %26 = getelementptr inbounds %struct.Edge, ptr %22, i64 %23, i32 2
  store i32 %25, ptr %26, align 8, !tbaa !17
  store i32 %.pre-phi, ptr %6, align 8, !tbaa !12
  %27 = getelementptr inbounds %struct.Edge, ptr %22, i64 %23, i32 1
  store i64 %2, ptr %27, align 8, !tbaa !18
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

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @SiftDown(ptr nocapture noundef %0, i32 noundef %1) local_unnamed_addr #5 {
  %3 = getelementptr inbounds i8, ptr %0, i64 400004
  br label %4

4:                                                ; preds = %47, %2
  %5 = phi i32 [ %1, %2 ], [ %45, %47 ]
  %6 = shl nsw i32 %5, 1
  %7 = or disjoint i32 %6, 1
  %8 = add nsw i32 %6, 2
  %9 = load i32, ptr %3, align 4, !tbaa !19
  %10 = icmp slt i32 %7, %9
  br i1 %10, label %11, label %26

11:                                               ; preds = %4
  %12 = sext i32 %7 to i64
  %13 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %12
  %14 = load i32, ptr %13, align 4, !tbaa !16
  %15 = sext i32 %14 to i64
  %16 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %15
  %17 = load i64, ptr %16, align 8, !tbaa !21
  %18 = sext i32 %5 to i64
  %19 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %18
  %20 = load i32, ptr %19, align 4, !tbaa !16
  %21 = sext i32 %20 to i64
  %22 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %21
  %23 = load i64, ptr %22, align 8, !tbaa !21
  %24 = icmp ult i64 %17, %23
  %25 = select i1 %24, i32 %7, i32 %5
  br label %26

26:                                               ; preds = %11, %4
  %27 = phi i32 [ %5, %4 ], [ %25, %11 ]
  %28 = icmp slt i32 %8, %9
  br i1 %28, label %29, label %44

29:                                               ; preds = %26
  %30 = sext i32 %8 to i64
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
  %43 = select i1 %42, i32 %8, i32 %27
  br label %44

44:                                               ; preds = %29, %26
  %45 = phi i32 [ %27, %26 ], [ %43, %29 ]
  %46 = icmp eq i32 %45, %5
  br i1 %46, label %60, label %47

47:                                               ; preds = %44
  %48 = sext i32 %5 to i64
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
  store i32 %5, ptr %56, align 4, !tbaa !16
  %57 = load i32, ptr %51, align 4, !tbaa !16
  %58 = sext i32 %57 to i64
  %59 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %58
  store i32 %45, ptr %59, align 4, !tbaa !16
  br label %4

60:                                               ; preds = %44
  ret void
}

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @SiftUp(ptr nocapture noundef %0, i32 noundef %1) local_unnamed_addr #5 {
  %3 = icmp sgt i32 %1, 0
  br i1 %3, label %.preheader.preheader, label %.loopexit

.preheader.preheader:                             ; preds = %2
  %.phi.trans.insert = zext nneg i32 %1 to i64
  %.phi.trans.insert1 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %.phi.trans.insert
  %.pre = load i32, ptr %.phi.trans.insert1, align 4, !tbaa !16
  br label %.preheader

.preheader:                                       ; preds = %.preheader.preheader, %18
  %4 = phi i32 [ %25, %18 ], [ %.pre, %.preheader.preheader ]
  %5 = phi i32 [ %10, %18 ], [ %1, %.preheader.preheader ]
  %6 = sext i32 %4 to i64
  %7 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %6
  %8 = load i64, ptr %7, align 8, !tbaa !21
  %9 = add nsw i32 %5, -1
  %10 = lshr i32 %9, 1
  %11 = zext nneg i32 %10 to i64
  %12 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %11
  %13 = load i32, ptr %12, align 4, !tbaa !16
  %14 = sext i32 %13 to i64
  %15 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %14
  %16 = load i64, ptr %15, align 8, !tbaa !21
  %17 = icmp ult i64 %8, %16
  br i1 %17, label %18, label %.loopexit

18:                                               ; preds = %.preheader
  %19 = zext nneg i32 %5 to i64
  %20 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %19
  %21 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %6
  store i32 %10, ptr %21, align 4, !tbaa !16
  %22 = load i32, ptr %12, align 4, !tbaa !16
  %23 = sext i32 %22 to i64
  %24 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %23
  store i32 %5, ptr %24, align 4, !tbaa !16
  %25 = load i32, ptr %20, align 4, !tbaa !16
  %26 = load i32, ptr %12, align 4, !tbaa !16
  store i32 %26, ptr %20, align 4, !tbaa !16
  store i32 %25, ptr %12, align 4, !tbaa !16
  %27 = icmp ult i32 %9, 2
  br i1 %27, label %.loopexit, label %.preheader, !llvm.loop !22

.loopexit:                                        ; preds = %18, %.preheader, %2
  ret void
}

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local i32 @pop(ptr nocapture noundef %0) local_unnamed_addr #5 {
  %2 = load i32, ptr %0, align 4, !tbaa !16
  %3 = getelementptr inbounds i8, ptr %0, i64 400004
  %4 = load i32, ptr %3, align 4, !tbaa !19
  %5 = add nsw i32 %4, -1
  store i32 %5, ptr %3, align 4, !tbaa !19
  %6 = sext i32 %5 to i64
  %7 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %6
  %8 = load i32, ptr %7, align 4, !tbaa !16
  store i32 %8, ptr %0, align 4, !tbaa !16
  br label %9

9:                                                ; preds = %53, %1
  %.sink3 = phi i32 [ %63, %53 ], [ %8, %1 ]
  %.sink = phi i32 [ %51, %53 ], [ 0, %1 ]
  %10 = sext i32 %.sink3 to i64
  %11 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %10
  store i32 %.sink, ptr %11, align 4, !tbaa !16
  %12 = shl nsw i32 %.sink, 1
  %13 = or disjoint i32 %12, 1
  %14 = add nsw i32 %12, 2
  %15 = load i32, ptr %3, align 4, !tbaa !19
  %16 = icmp slt i32 %13, %15
  br i1 %16, label %17, label %32

17:                                               ; preds = %9
  %18 = sext i32 %13 to i64
  %19 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %18
  %20 = load i32, ptr %19, align 4, !tbaa !16
  %21 = sext i32 %20 to i64
  %22 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %21
  %23 = load i64, ptr %22, align 8, !tbaa !21
  %24 = sext i32 %.sink to i64
  %25 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %24
  %26 = load i32, ptr %25, align 4, !tbaa !16
  %27 = sext i32 %26 to i64
  %28 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %27
  %29 = load i64, ptr %28, align 8, !tbaa !21
  %30 = icmp ult i64 %23, %29
  %31 = select i1 %30, i32 %13, i32 %.sink
  br label %32

32:                                               ; preds = %17, %9
  %33 = phi i32 [ %.sink, %9 ], [ %31, %17 ]
  %34 = icmp slt i32 %14, %15
  br i1 %34, label %35, label %50

35:                                               ; preds = %32
  %36 = sext i32 %14 to i64
  %37 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %36
  %38 = load i32, ptr %37, align 4, !tbaa !16
  %39 = sext i32 %38 to i64
  %40 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %39
  %41 = load i64, ptr %40, align 8, !tbaa !21
  %42 = sext i32 %33 to i64
  %43 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %42
  %44 = load i32, ptr %43, align 4, !tbaa !16
  %45 = sext i32 %44 to i64
  %46 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %45
  %47 = load i64, ptr %46, align 8, !tbaa !21
  %48 = icmp ult i64 %41, %47
  %49 = select i1 %48, i32 %14, i32 %33
  br label %50

50:                                               ; preds = %35, %32
  %51 = phi i32 [ %33, %32 ], [ %49, %35 ]
  %52 = icmp eq i32 %51, %.sink
  br i1 %52, label %SiftDown.exit, label %53

53:                                               ; preds = %50
  %54 = sext i32 %.sink to i64
  %55 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %54
  %56 = sext i32 %51 to i64
  %57 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %56
  %58 = load i32, ptr %55, align 4, !tbaa !16
  %59 = load i32, ptr %57, align 4, !tbaa !16
  store i32 %59, ptr %55, align 4, !tbaa !16
  store i32 %58, ptr %57, align 4, !tbaa !16
  %60 = load i32, ptr %55, align 4, !tbaa !16
  %61 = sext i32 %60 to i64
  %62 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %61
  store i32 %.sink, ptr %62, align 4, !tbaa !16
  %63 = load i32, ptr %57, align 4, !tbaa !16
  br label %9

SiftDown.exit:                                    ; preds = %50
  ret i32 %2
}

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @push(ptr nocapture noundef %0, i32 noundef %1) local_unnamed_addr #5 {
  %3 = getelementptr inbounds i8, ptr %0, i64 400004
  %4 = load i32, ptr %3, align 4, !tbaa !19
  %5 = sext i32 %1 to i64
  %6 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %5
  store i32 %4, ptr %6, align 4, !tbaa !16
  %7 = add nsw i32 %4, 1
  store i32 %7, ptr %3, align 4, !tbaa !19
  %8 = sext i32 %4 to i64
  %9 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %8
  store i32 %1, ptr %9, align 4, !tbaa !16
  %10 = load i32, ptr %3, align 4, !tbaa !19
  %11 = icmp sgt i32 %10, 1
  br i1 %11, label %12, label %.loopexit

12:                                               ; preds = %2
  %13 = add nsw i32 %10, -1
  %.phi.trans.insert = zext nneg i32 %13 to i64
  %.phi.trans.insert1 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %.phi.trans.insert
  %.pre = load i32, ptr %.phi.trans.insert1, align 4, !tbaa !16
  br label %14

14:                                               ; preds = %29, %12
  %15 = phi i32 [ %36, %29 ], [ %.pre, %12 ]
  %16 = phi i32 [ %21, %29 ], [ %13, %12 ]
  %17 = sext i32 %15 to i64
  %18 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %17
  %19 = load i64, ptr %18, align 8, !tbaa !21
  %20 = add nsw i32 %16, -1
  %21 = lshr i32 %20, 1
  %22 = zext nneg i32 %21 to i64
  %23 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %22
  %24 = load i32, ptr %23, align 4, !tbaa !16
  %25 = sext i32 %24 to i64
  %26 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %25
  %27 = load i64, ptr %26, align 8, !tbaa !21
  %28 = icmp ult i64 %19, %27
  br i1 %28, label %29, label %.loopexit

29:                                               ; preds = %14
  %30 = zext nneg i32 %16 to i64
  %31 = getelementptr inbounds [100001 x i32], ptr %0, i64 0, i64 %30
  %32 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %17
  store i32 %21, ptr %32, align 4, !tbaa !16
  %33 = load i32, ptr %23, align 4, !tbaa !16
  %34 = sext i32 %33 to i64
  %35 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %34
  store i32 %16, ptr %35, align 4, !tbaa !16
  %36 = load i32, ptr %31, align 4, !tbaa !16
  %37 = load i32, ptr %23, align 4, !tbaa !16
  store i32 %37, ptr %31, align 4, !tbaa !16
  store i32 %36, ptr %23, align 4, !tbaa !16
  %38 = icmp ult i32 %20, 2
  br i1 %38, label %.loopexit, label %14, !llvm.loop !22

.loopexit:                                        ; preds = %29, %14, %2
  ret void
}

; Function Attrs: nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable
define dso_local void @dijkstra(i32 noundef %0, ptr nocapture noundef %1) local_unnamed_addr #5 {
  %3 = load i32, ptr @N, align 4, !tbaa !16
  %4 = icmp slt i32 %3, 1
  br i1 %4, label %.thread, label %7

.thread:                                          ; preds = %2
  %5 = sext i32 %0 to i64
  %6 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %5
  store i64 0, ptr %6, align 8, !tbaa !21
  br label %.loopexit11

7:                                                ; preds = %2
  %8 = add nuw i32 %3, 1
  %9 = zext i32 %8 to i64
  %10 = zext nneg i32 %3 to i64
  %min.iters.check = icmp eq i32 %3, 1
  br i1 %min.iters.check, label %scalar.ph.preheader, label %vector.ph

scalar.ph.preheader:                              ; preds = %middle.block, %7
  %.ph = phi i64 [ 1, %7 ], [ %ind.end, %middle.block ]
  br label %scalar.ph

vector.ph:                                        ; preds = %7
  %n.vec = and i64 %10, 2147483646
  %ind.end = or i64 %10, 1
  br label %vector.body

vector.body:                                      ; preds = %vector.body, %vector.ph
  %index = phi i64 [ 0, %vector.ph ], [ %index.next, %vector.body ]
  %offset.idx = or disjoint i64 %index, 1
  %11 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %offset.idx
  store <2 x i64> <i64 60000000001, i64 60000000001>, ptr %11, align 8, !tbaa !21
  %index.next = add nuw i64 %index, 2
  %12 = icmp eq i64 %index.next, %n.vec
  br i1 %12, label %middle.block, label %vector.body, !llvm.loop !25

middle.block:                                     ; preds = %vector.body
  %cmp.n = icmp eq i64 %n.vec, %10
  br i1 %cmp.n, label %.loopexit21, label %scalar.ph.preheader

.loopexit21:                                      ; preds = %scalar.ph, %middle.block
  %13 = sext i32 %0 to i64
  %14 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %13
  store i64 0, ptr %14, align 8, !tbaa !21
  %15 = getelementptr inbounds i8, ptr %1, i64 400004
  br label %23

scalar.ph:                                        ; preds = %scalar.ph.preheader, %scalar.ph
  %16 = phi i64 [ %18, %scalar.ph ], [ %.ph, %scalar.ph.preheader ]
  %17 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %16
  store i64 60000000001, ptr %17, align 8, !tbaa !21
  %18 = add nuw nsw i64 %16, 1
  %19 = icmp eq i64 %18, %9
  br i1 %19, label %.loopexit21, label %scalar.ph, !llvm.loop !28

.loopexit11:                                      ; preds = %.loopexit10, %.thread
  %20 = getelementptr inbounds i8, ptr %1, i64 400004
  %21 = load i32, ptr %20, align 4, !tbaa !19
  %22 = icmp eq i32 %21, 0
  br i1 %22, label %.loopexit9, label %.preheader8

23:                                               ; preds = %.loopexit10, %.loopexit21
  %24 = phi i64 [ 1, %.loopexit21 ], [ %60, %.loopexit10 ]
  %25 = load i32, ptr %15, align 4, !tbaa !19
  %26 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %24
  store i32 %25, ptr %26, align 4, !tbaa !16
  %27 = add nsw i32 %25, 1
  store i32 %27, ptr %15, align 4, !tbaa !19
  %28 = sext i32 %25 to i64
  %29 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %28
  %30 = trunc i64 %24 to i32
  store i32 %30, ptr %29, align 4, !tbaa !16
  %31 = load i32, ptr %15, align 4, !tbaa !19
  %32 = icmp sgt i32 %31, 1
  br i1 %32, label %33, label %.loopexit10

33:                                               ; preds = %23
  %34 = add nsw i32 %31, -1
  %.phi.trans.insert = zext nneg i32 %34 to i64
  %.phi.trans.insert12 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %.phi.trans.insert
  %.pre = load i32, ptr %.phi.trans.insert12, align 4, !tbaa !16
  br label %35

35:                                               ; preds = %50, %33
  %36 = phi i32 [ %57, %50 ], [ %.pre, %33 ]
  %37 = phi i32 [ %42, %50 ], [ %34, %33 ]
  %38 = sext i32 %36 to i64
  %39 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %38
  %40 = load i64, ptr %39, align 8, !tbaa !21
  %41 = add nsw i32 %37, -1
  %42 = lshr i32 %41, 1
  %43 = zext nneg i32 %42 to i64
  %44 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %43
  %45 = load i32, ptr %44, align 4, !tbaa !16
  %46 = sext i32 %45 to i64
  %47 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %46
  %48 = load i64, ptr %47, align 8, !tbaa !21
  %49 = icmp ult i64 %40, %48
  br i1 %49, label %50, label %.loopexit10

50:                                               ; preds = %35
  %51 = zext nneg i32 %37 to i64
  %52 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %51
  %53 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %38
  store i32 %42, ptr %53, align 4, !tbaa !16
  %54 = load i32, ptr %44, align 4, !tbaa !16
  %55 = sext i32 %54 to i64
  %56 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %55
  store i32 %37, ptr %56, align 4, !tbaa !16
  %57 = load i32, ptr %52, align 4, !tbaa !16
  %58 = load i32, ptr %44, align 4, !tbaa !16
  store i32 %58, ptr %52, align 4, !tbaa !16
  store i32 %57, ptr %44, align 4, !tbaa !16
  %59 = icmp ult i32 %41, 2
  br i1 %59, label %.loopexit10, label %35, !llvm.loop !22

.loopexit10:                                      ; preds = %50, %35, %23
  %60 = add nuw nsw i64 %24, 1
  %61 = load i32, ptr @N, align 4, !tbaa !16
  %62 = sext i32 %61 to i64
  %63 = icmp slt i64 %24, %62
  br i1 %63, label %23, label %.loopexit11, !llvm.loop !29

.loopexit7.loopexit:                              ; preds = %.loopexit
  %.pre17 = load i32, ptr %20, align 4, !tbaa !19
  br label %.loopexit7

.loopexit7:                                       ; preds = %.loopexit7.loopexit, %SiftDown.exit
  %64 = phi i32 [ %.pre17, %.loopexit7.loopexit ], [ %78, %SiftDown.exit ]
  %65 = icmp eq i32 %64, 0
  br i1 %65, label %.loopexit9, label %.preheader8, !llvm.loop !30

.preheader8:                                      ; preds = %.loopexit11, %.loopexit7
  %66 = phi i32 [ %64, %.loopexit7 ], [ %21, %.loopexit11 ]
  %67 = load i32, ptr %1, align 4, !tbaa !16
  %68 = add nsw i32 %66, -1
  store i32 %68, ptr %20, align 4, !tbaa !19
  %69 = sext i32 %68 to i64
  %70 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %69
  %71 = load i32, ptr %70, align 4, !tbaa !16
  store i32 %71, ptr %1, align 4, !tbaa !16
  br label %72

72:                                               ; preds = %116, %.preheader8
  %.sink20 = phi i32 [ %126, %116 ], [ %71, %.preheader8 ]
  %.sink = phi i32 [ %114, %116 ], [ 0, %.preheader8 ]
  %73 = sext i32 %.sink20 to i64
  %74 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %73
  store i32 %.sink, ptr %74, align 4, !tbaa !16
  %75 = shl nsw i32 %.sink, 1
  %76 = or disjoint i32 %75, 1
  %77 = add nsw i32 %75, 2
  %78 = load i32, ptr %20, align 4, !tbaa !19
  %79 = icmp slt i32 %76, %78
  br i1 %79, label %80, label %95

80:                                               ; preds = %72
  %81 = sext i32 %76 to i64
  %82 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %81
  %83 = load i32, ptr %82, align 4, !tbaa !16
  %84 = sext i32 %83 to i64
  %85 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %84
  %86 = load i64, ptr %85, align 8, !tbaa !21
  %87 = sext i32 %.sink to i64
  %88 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %87
  %89 = load i32, ptr %88, align 4, !tbaa !16
  %90 = sext i32 %89 to i64
  %91 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %90
  %92 = load i64, ptr %91, align 8, !tbaa !21
  %93 = icmp ult i64 %86, %92
  %94 = select i1 %93, i32 %76, i32 %.sink
  br label %95

95:                                               ; preds = %80, %72
  %96 = phi i32 [ %.sink, %72 ], [ %94, %80 ]
  %97 = icmp slt i32 %77, %78
  br i1 %97, label %98, label %113

98:                                               ; preds = %95
  %99 = sext i32 %77 to i64
  %100 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %99
  %101 = load i32, ptr %100, align 4, !tbaa !16
  %102 = sext i32 %101 to i64
  %103 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %102
  %104 = load i64, ptr %103, align 8, !tbaa !21
  %105 = sext i32 %96 to i64
  %106 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %105
  %107 = load i32, ptr %106, align 4, !tbaa !16
  %108 = sext i32 %107 to i64
  %109 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %108
  %110 = load i64, ptr %109, align 8, !tbaa !21
  %111 = icmp ult i64 %104, %110
  %112 = select i1 %111, i32 %77, i32 %96
  br label %113

113:                                              ; preds = %98, %95
  %114 = phi i32 [ %96, %95 ], [ %112, %98 ]
  %115 = icmp eq i32 %114, %.sink
  br i1 %115, label %SiftDown.exit, label %116

116:                                              ; preds = %113
  %117 = sext i32 %.sink to i64
  %118 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %117
  %119 = sext i32 %114 to i64
  %120 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %119
  %121 = load i32, ptr %118, align 4, !tbaa !16
  %122 = load i32, ptr %120, align 4, !tbaa !16
  store i32 %122, ptr %118, align 4, !tbaa !16
  store i32 %121, ptr %120, align 4, !tbaa !16
  %123 = load i32, ptr %118, align 4, !tbaa !16
  %124 = sext i32 %123 to i64
  %125 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %124
  store i32 %.sink, ptr %125, align 4, !tbaa !16
  %126 = load i32, ptr %120, align 4, !tbaa !16
  br label %72

SiftDown.exit:                                    ; preds = %113
  %127 = sext i32 %67 to i64
  %128 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %127, i32 1
  %129 = load i32, ptr %128, align 8, !tbaa !12
  %130 = icmp sgt i32 %129, 0
  br i1 %130, label %131, label %.loopexit7

131:                                              ; preds = %SiftDown.exit
  %132 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %127
  %133 = load ptr, ptr %132, align 16, !tbaa !11
  %134 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %127
  br label %135

135:                                              ; preds = %.loopexit, %131
  %136 = phi i32 [ %129, %131 ], [ %182, %.loopexit ]
  %137 = phi i64 [ 0, %131 ], [ %183, %.loopexit ]
  %138 = getelementptr inbounds %struct.Edge, ptr %133, i64 %137
  %139 = load i32, ptr %138, align 8, !tbaa !13
  %140 = sext i32 %139 to i64
  %141 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %140
  %142 = load i64, ptr %141, align 8, !tbaa !21
  %143 = load i64, ptr %134, align 8, !tbaa !21
  %144 = getelementptr inbounds %struct.Edge, ptr %133, i64 %137, i32 1
  %145 = load i64, ptr %144, align 8, !tbaa !18
  %146 = add i64 %145, %143
  %147 = icmp ugt i64 %142, %146
  br i1 %147, label %148, label %.loopexit

148:                                              ; preds = %135
  store i64 %146, ptr %141, align 8, !tbaa !21
  %149 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %140
  store i32 %67, ptr %149, align 8, !tbaa !13
  %150 = getelementptr inbounds %struct.Edge, ptr %133, i64 %137, i32 2
  %151 = load i32, ptr %150, align 8, !tbaa !17
  %152 = load i32, ptr %138, align 8, !tbaa !13
  %153 = sext i32 %152 to i64
  %154 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %153, i32 2
  store i32 %151, ptr %154, align 8, !tbaa !17
  %155 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %153
  %156 = load i32, ptr %155, align 4, !tbaa !16
  %157 = icmp sgt i32 %156, 0
  br i1 %157, label %.preheader.preheader, label %.loopexit

.preheader.preheader:                             ; preds = %148
  %.phi.trans.insert13 = zext nneg i32 %156 to i64
  %.phi.trans.insert14 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %.phi.trans.insert13
  %.pre15 = load i32, ptr %.phi.trans.insert14, align 4, !tbaa !16
  br label %.preheader

.preheader:                                       ; preds = %.preheader.preheader, %172
  %158 = phi i32 [ %179, %172 ], [ %.pre15, %.preheader.preheader ]
  %159 = phi i32 [ %164, %172 ], [ %156, %.preheader.preheader ]
  %160 = sext i32 %158 to i64
  %161 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %160
  %162 = load i64, ptr %161, align 8, !tbaa !21
  %163 = add nsw i32 %159, -1
  %164 = lshr i32 %163, 1
  %165 = zext nneg i32 %164 to i64
  %166 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %165
  %167 = load i32, ptr %166, align 4, !tbaa !16
  %168 = sext i32 %167 to i64
  %169 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %168
  %170 = load i64, ptr %169, align 8, !tbaa !21
  %171 = icmp ult i64 %162, %170
  br i1 %171, label %172, label %.loopexit.loopexit

172:                                              ; preds = %.preheader
  %173 = zext nneg i32 %159 to i64
  %174 = getelementptr inbounds [100001 x i32], ptr %1, i64 0, i64 %173
  %175 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %160
  store i32 %164, ptr %175, align 4, !tbaa !16
  %176 = load i32, ptr %166, align 4, !tbaa !16
  %177 = sext i32 %176 to i64
  %178 = getelementptr inbounds [100001 x i32], ptr @num, i64 0, i64 %177
  store i32 %159, ptr %178, align 4, !tbaa !16
  %179 = load i32, ptr %174, align 4, !tbaa !16
  %180 = load i32, ptr %166, align 4, !tbaa !16
  store i32 %180, ptr %174, align 4, !tbaa !16
  store i32 %179, ptr %166, align 4, !tbaa !16
  %181 = icmp ult i32 %163, 2
  br i1 %181, label %.loopexit.loopexit, label %.preheader, !llvm.loop !22

.loopexit.loopexit:                               ; preds = %.preheader, %172
  %.pre16 = load i32, ptr %128, align 8, !tbaa !12
  br label %.loopexit

.loopexit:                                        ; preds = %.loopexit.loopexit, %148, %135
  %182 = phi i32 [ %.pre16, %.loopexit.loopexit ], [ %136, %148 ], [ %136, %135 ]
  %183 = add nuw nsw i64 %137, 1
  %184 = sext i32 %182 to i64
  %185 = icmp slt i64 %183, %184
  br i1 %185, label %135, label %.loopexit7.loopexit, !llvm.loop !31

.loopexit9:                                       ; preds = %.loopexit7, %.loopexit11
  ret void
}

; Function Attrs: nounwind uwtable
define dso_local noundef i32 @main() local_unnamed_addr #6 {
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
  br i1 %10, label %.preheader9, label %.loopexit10

.loopexit10:                                      ; preds = %.preheader9, %0
  %11 = phi i32 [ %9, %0 ], [ %21, %.preheader9 ]
  %12 = load i32, ptr @M, align 4, !tbaa !16
  %13 = icmp sgt i32 %12, 0
  br i1 %13, label %.preheader7, label %.loopexit8

.preheader9:                                      ; preds = %0, %.preheader9
  %14 = phi i64 [ %20, %.preheader9 ], [ 0, %0 ]
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %1) #11
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %2) #11
  %15 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef nonnull @.str.1, ptr noundef nonnull %1, ptr noundef nonnull %2)
  %16 = load i32, ptr %1, align 4, !tbaa !16
  %17 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %14
  store i32 %16, ptr %17, align 8, !tbaa !32
  %18 = load i32, ptr %2, align 4, !tbaa !16
  %19 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %14, i32 1
  store i32 %18, ptr %19, align 4, !tbaa !34
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %2) #11
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %1) #11
  %20 = add nuw nsw i64 %14, 1
  %21 = load i32, ptr @K, align 4, !tbaa !16
  %22 = sext i32 %21 to i64
  %23 = icmp slt i64 %20, %22
  br i1 %23, label %.preheader9, label %.loopexit10, !llvm.loop !35

.loopexit8.loopexit:                              ; preds = %51
  %.pre12 = load i32, ptr @K, align 4, !tbaa !16
  br label %.loopexit8

.loopexit8:                                       ; preds = %.loopexit8.loopexit, %.loopexit10
  %24 = phi i32 [ %.pre12, %.loopexit8.loopexit ], [ %11, %.loopexit10 ]
  %25 = icmp sgt i32 %24, 0
  br i1 %25, label %26, label %.loopexit6

26:                                               ; preds = %.loopexit8
  %27 = getelementptr inbounds i8, ptr %6, i64 400004
  br label %63

.preheader7:                                      ; preds = %.loopexit10, %51
  %28 = phi i32 [ %60, %51 ], [ 0, %.loopexit10 ]
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %3) #11
  call void @llvm.lifetime.start.p0(i64 4, ptr nonnull %4) #11
  call void @llvm.lifetime.start.p0(i64 8, ptr nonnull %5) #11
  %29 = call i32 (ptr, ...) @__isoc99_scanf(ptr noundef nonnull @.str.2, ptr noundef nonnull %3, ptr noundef nonnull %4, ptr noundef nonnull %5)
  %30 = load i32, ptr %3, align 4, !tbaa !16
  %31 = sext i32 %30 to i64
  %32 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %31
  %33 = load i32, ptr %4, align 4, !tbaa !16
  %34 = load i64, ptr %5, align 8, !tbaa !36
  %35 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %31, i32 2
  %36 = load i32, ptr %35, align 4, !tbaa !5
  %37 = getelementptr inbounds [100001 x %struct.vector], ptr @verticals, i64 0, i64 %31, i32 1
  %38 = load i32, ptr %37, align 8, !tbaa !12
  %39 = add nsw i32 %38, 1
  %40 = icmp sgt i32 %36, %39
  br i1 %40, label %.preheader7._crit_edge, label %41

.preheader7._crit_edge:                           ; preds = %.preheader7
  %.pre = load ptr, ptr %32, align 16, !tbaa !11
  br label %51

41:                                               ; preds = %.preheader7
  %42 = icmp eq i32 %36, 0
  br i1 %42, label %43, label %45

43:                                               ; preds = %41
  %44 = call noalias dereferenceable_or_null(24) ptr @malloc(i64 noundef 24) #9
  store ptr %44, ptr %32, align 16, !tbaa !11
  store i32 1, ptr %35, align 4, !tbaa !5
  br label %51

45:                                               ; preds = %41
  %46 = shl nsw i32 %36, 1
  store i32 %46, ptr %35, align 4, !tbaa !5
  %47 = load ptr, ptr %32, align 16, !tbaa !11
  %48 = sext i32 %46 to i64
  %49 = mul nsw i64 %48, 24
  %50 = call ptr @realloc(ptr noundef %47, i64 noundef %49) #10
  store ptr %50, ptr %32, align 16, !tbaa !11
  %.pre11 = load i32, ptr %37, align 8, !tbaa !12
  %.pre13 = add nsw i32 %.pre11, 1
  br label %51

51:                                               ; preds = %.preheader7._crit_edge, %45, %43
  %.pre-phi = phi i32 [ %39, %.preheader7._crit_edge ], [ %.pre13, %45 ], [ %39, %43 ]
  %52 = phi i32 [ %38, %.preheader7._crit_edge ], [ %.pre11, %45 ], [ %38, %43 ]
  %53 = phi ptr [ %.pre, %.preheader7._crit_edge ], [ %50, %45 ], [ %44, %43 ]
  %54 = sext i32 %52 to i64
  %55 = getelementptr inbounds %struct.Edge, ptr %53, i64 %54
  store i32 %33, ptr %55, align 8, !tbaa !13
  %56 = load i32, ptr @ind, align 4, !tbaa !16
  %57 = getelementptr inbounds %struct.Edge, ptr %53, i64 %54, i32 2
  store i32 %56, ptr %57, align 8, !tbaa !17
  store i32 %.pre-phi, ptr %37, align 8, !tbaa !12
  %58 = getelementptr inbounds %struct.Edge, ptr %53, i64 %54, i32 1
  store i64 %34, ptr %58, align 8, !tbaa !18
  %59 = add nsw i32 %56, 1
  store i32 %59, ptr @ind, align 4, !tbaa !16
  call void @llvm.lifetime.end.p0(i64 8, ptr nonnull %5) #11
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %4) #11
  call void @llvm.lifetime.end.p0(i64 4, ptr nonnull %3) #11
  %60 = add nuw nsw i32 %28, 1
  %61 = load i32, ptr @M, align 4, !tbaa !16
  %62 = icmp slt i32 %60, %61
  br i1 %62, label %.preheader7, label %.loopexit8.loopexit, !llvm.loop !38

.loopexit6:                                       ; preds = %105, %.loopexit8
  ret i32 0

63:                                               ; preds = %105, %26
  %64 = phi i64 [ 0, %26 ], [ %106, %105 ]
  call void @llvm.lifetime.start.p0(i64 400008, ptr nonnull %6) #11
  store i32 0, ptr %27, align 4, !tbaa !19
  %65 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %64
  %66 = load i32, ptr %65, align 8, !tbaa !32
  call void @dijkstra(i32 noundef %66, ptr noundef nonnull %6)
  %67 = getelementptr inbounds [21 x %struct.pair], ptr @request, i64 0, i64 %64, i32 1
  %68 = load i32, ptr %67, align 4, !tbaa !34
  %69 = sext i32 %68 to i64
  %70 = getelementptr inbounds [100001 x i64], ptr @dist1, i64 0, i64 %69
  %71 = load i64, ptr %70, align 8, !tbaa !21
  %72 = icmp eq i64 %71, 60000000001
  br i1 %72, label %103, label %73

73:                                               ; preds = %63
  %74 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.3, i64 noundef %71)
  %75 = load i32, ptr %65, align 8, !tbaa !32
  %76 = load i32, ptr %67, align 4, !tbaa !34
  call void @llvm.lifetime.start.p0(i64 400004, ptr nonnull %7) #11
  %77 = icmp eq i32 %75, %76
  br i1 %77, label %.thread, label %.preheader

.thread:                                          ; preds = %73
  %78 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.4, i32 noundef 0)
  br label %.loopexit

.preheader:                                       ; preds = %73, %.preheader
  %79 = phi i64 [ %85, %.preheader ], [ 0, %73 ]
  %80 = phi i32 [ %87, %.preheader ], [ %76, %73 ]
  %81 = sext i32 %80 to i64
  %82 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %81
  %83 = getelementptr inbounds [100001 x %struct.Edge], ptr @F, i64 0, i64 %81, i32 2
  %84 = load i32, ptr %83, align 8, !tbaa !17
  %85 = add nuw i64 %79, 1
  %86 = getelementptr inbounds [100001 x i32], ptr %7, i64 0, i64 %79
  store i32 %84, ptr %86, align 4, !tbaa !16
  %87 = load i32, ptr %82, align 8, !tbaa !13
  %88 = icmp eq i32 %75, %87
  br i1 %88, label %89, label %.preheader, !llvm.loop !39

89:                                               ; preds = %.preheader
  %90 = trunc i64 %85 to i32
  %91 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.4, i32 noundef %90)
  %92 = icmp sgt i32 %90, 0
  br i1 %92, label %93, label %.loopexit

93:                                               ; preds = %89
  %94 = and i64 %85, 2147483647
  br label %96

.loopexit:                                        ; preds = %96, %.thread, %89
  %95 = call i32 @putchar(i32 10)
  call void @llvm.lifetime.end.p0(i64 400004, ptr nonnull %7) #11
  br label %105

96:                                               ; preds = %96, %93
  %97 = phi i64 [ %94, %93 ], [ %98, %96 ]
  %98 = add nsw i64 %97, -1
  %99 = getelementptr inbounds [100001 x i32], ptr %7, i64 0, i64 %98
  %100 = load i32, ptr %99, align 4, !tbaa !16
  %101 = call i32 (ptr, ...) @printf(ptr noundef nonnull dereferenceable(1) @.str.4, i32 noundef %100)
  %102 = icmp ugt i64 %97, 1
  br i1 %102, label %96, label %.loopexit, !llvm.loop !40

103:                                              ; preds = %63
  %104 = call i32 @puts(ptr nonnull dereferenceable(1) @str)
  br label %105

105:                                              ; preds = %103, %.loopexit
  call void @llvm.lifetime.end.p0(i64 400008, ptr nonnull %6) #11
  %106 = add nuw nsw i64 %64, 1
  %107 = load i32, ptr @K, align 4, !tbaa !16
  %108 = sext i32 %107 to i64
  %109 = icmp slt i64 %106, %108
  br i1 %109, label %63, label %.loopexit6, !llvm.loop !41
}

; Function Attrs: nofree nounwind
declare noundef i32 @__isoc99_scanf(ptr nocapture noundef readonly, ...) local_unnamed_addr #7

; Function Attrs: nofree nounwind
declare noundef i32 @printf(ptr nocapture noundef readonly, ...) local_unnamed_addr #7

; Function Attrs: nofree nounwind
declare noundef i32 @puts(ptr nocapture noundef readonly) local_unnamed_addr #8

; Function Attrs: nofree nounwind
declare noundef i32 @putchar(i32 noundef) local_unnamed_addr #8

attributes #0 = { mustprogress nounwind willreturn uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #1 = { mustprogress nofree nounwind willreturn allockind("alloc,uninitialized") allocsize(0) memory(inaccessiblemem: readwrite) "alloc-family"="malloc" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #2 = { mustprogress nounwind willreturn allockind("realloc") allocsize(1) memory(argmem: readwrite, inaccessiblemem: readwrite) "alloc-family"="malloc" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #3 = { mustprogress nofree norecurse nosync nounwind willreturn memory(argmem: readwrite) uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #4 = { mustprogress nocallback nofree nosync nounwind willreturn memory(argmem: readwrite) }
attributes #5 = { nofree norecurse nosync nounwind memory(readwrite, inaccessiblemem: none) uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #6 = { nounwind uwtable "min-legal-vector-width"="0" "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #7 = { nofree nounwind "no-trapping-math"="true" "stack-protector-buffer-size"="8" "target-cpu"="x86-64" "target-features"="+cmov,+cx8,+fxsr,+mmx,+sse,+sse2,+x87" "tune-cpu"="generic" }
attributes #8 = { nofree nounwind }
attributes #9 = { nounwind allocsize(0) }
attributes #10 = { nounwind allocsize(1) }
attributes #11 = { nounwind }

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
!25 = distinct !{!25, !23, !24, !26, !27}
!26 = !{!"llvm.loop.isvectorized", i32 1}
!27 = !{!"llvm.loop.unroll.runtime.disable"}
!28 = distinct !{!28, !23, !24, !26}
!29 = distinct !{!29, !23, !24}
!30 = distinct !{!30, !23, !24}
!31 = distinct !{!31, !23, !24}
!32 = !{!33, !10, i64 0}
!33 = !{!"pair", !10, i64 0, !10, i64 4}
!34 = !{!33, !10, i64 4}
!35 = distinct !{!35, !23, !24}
!36 = !{!37, !37, i64 0}
!37 = !{!"long long", !8, i64 0}
!38 = distinct !{!38, !23, !24}
!39 = distinct !{!39, !23, !24}
!40 = distinct !{!40, !23, !24}
!41 = distinct !{!41, !23, !24}
