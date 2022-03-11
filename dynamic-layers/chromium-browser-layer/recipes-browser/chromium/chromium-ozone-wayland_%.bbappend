FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"

DEPENDS:remove = "compiler-rt-native libcxx-native"

BUILD_CPPFLAGS:remove = "-isysroot=${STAGING_DIR_NATIVE} -stdlib=libc++"
BUILD_LDFLAGS:remove = "-rtlib=libgcc -unwindlib=libgcc -stdlib=libc++ -lc++abi -rpath ${STAGING_LIBDIR_NATIVE}"
