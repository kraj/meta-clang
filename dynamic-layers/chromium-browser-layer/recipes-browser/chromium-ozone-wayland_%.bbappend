FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

GN_ARGS += "use_lld=true use_gold=false"

UNWINDLIB_toolchain-clang = "--unwindlib=libgcc"

DEPENDS_remove = "compiler-rt-native libcxx-native"

BUILD_CPPFLAGS_remove = "-isysroot=${STAGING_DIR_NATIVE} -stdlib=libc++"
BUILD_LDFLAGS_remove = "-rtlib=libgcc -unwindlib=libgcc -stdlib=libc++ -lc++abi -rpath ${STAGING_LIBDIR_NATIVE}"
