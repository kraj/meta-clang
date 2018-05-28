do_configure_append_toolchain-clang() {
	export NM="${HOST_PREFIX}nm"
	export AR="${HOST_PREFIX}ar"
	export RANLIB="${HOST_PREFIX}ranlib"
	export EFI_CC="${CC}"
	sed -i -e "s#O0#O#g" ${S}/src/boot/efi/meson.build
}

