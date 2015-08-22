do_configure_prepend() {
	sed -i -e '/-ftrapv/d' ${S}/configure.ac
}
