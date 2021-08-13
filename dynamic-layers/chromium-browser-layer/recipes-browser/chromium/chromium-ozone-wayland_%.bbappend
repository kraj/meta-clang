FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPEND_remove_runtime-llvm = "compiler-rt-native libcxx-native"

BUILD_CPPFLAGS_remove_rumtime-llvm = "-isysroot=${STAGING_DIR_NATIVE} -stdlib=libc++"
BUILD_LDFLAGS_remove_runtime-llvm = "-rtlib=libgcc -unwindlib=libgcc -stdlib=libc++ -lc++abi -rpath ${STAGING_LIBDIR_NATIVE}"
