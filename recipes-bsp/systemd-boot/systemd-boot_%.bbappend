do_configure_append_toolchain-clang() {
	export EFI_CC="${CC}"
	sed -i -e "s#O0#O#g" ${S}/src/boot/efi/meson.build
}
