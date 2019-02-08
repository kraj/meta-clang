# Enable LTO based on global distro settings
TOOLCHAIN_OPTIONS_append_toolchain-clang = "${@bb.utils.contains('DISTRO_FEATURES', 'thin-lto', ' -flto=thin -fuse-ld=gold', '', d)}"
TOOLCHAIN_OPTIONS_append_toolchain-clang = "${@bb.utils.contains('DISTRO_FEATURES', 'full-lto', ' -flto=full -fuse-ld=gold', '', d)}"
RANLIB_toolchain-clang = "${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ranlib"
AR_toolchain-clang = "${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-ar"
NM_toolchain-clang = "${STAGING_BINDIR_TOOLCHAIN}/${TARGET_PREFIX}llvm-nm"

