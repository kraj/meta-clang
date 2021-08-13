FILESEXTRAPATHS_prepend := "${THISDIR}/${PN}:"

DEPENDS_remove = "compiler-rt-native libcxx-native"

CXXFLAGS_remove = "-isysroot=${STAGING_DIR_NATIVE} -stdlib=libc++"
LDFLAGS_remove = "-rtlib=libgcc -unwindlib=libgcc -stdlib=libc++ -lc++abi -rpath ${STAGING_LIBDIR_NATIVE}"
