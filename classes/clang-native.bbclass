# inherit this class if you would like to use clang to compile the native
# version of your recipes instead of system compiler ( which is normally gcc )
# on build machines
# to use it add
#
# inherit clang-native
#
# to the concerned recipe via a bbappend or directly to recipe file
#
DEPENDS:append:runtime-llvm = " clang-native compiler-rt-native libcxx-native"
# Use libcxx headers for native parts
CXXFLAGS:append:runtime-llvm = " -stdlib=libc++"
BUILD_CXXFLAGS:append:runtime-llvm = " -isysroot=${STAGING_DIR_NATIVE} -stdlib=libc++"
# Use libgcc for native parts
LDFLAGS:append:runtime-llvm = " -stdlib=libc++ -rtlib=libgcc -unwindlib=libgcc"
BUILD_LDFLAGS:append:runtime-llvm = " -stdlib=libc++ -rtlib=libgcc -unwindlib=libgcc"
BUILD_CC:runtime-llvm  = "${CCACHE}clang -isysroot=${STAGING_DIR_NATIVE}"
BUILD_CXX:runtime-llvm = "${CCACHE}clang++ -isysroot=${STAGING_DIR_NATIVE}"
BUILD_CPP:runtime-llvm = "${CCACHE}clang -isysroot=${STAGING_DIR_NATIVE} -E"
BUILD_CCLD:runtime-llvm = "${CCACHE}clang"
BUILD_RANLIB:runtime-llvm = "llvm-ranlib"
BUILD_AR:runtime-llvm = "llvm-ar"
BUILD_NM:runtime-llvm = "llvm-nm"
