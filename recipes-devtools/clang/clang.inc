# LLVM does not provide ABI stability between different versions. For this
# reason OE makes it possible to build and install different llvm versions
# at the same time.
#
# This is true for the normal recipes as well as the native ones.
#
# All regular installation directories are prefixed with 'llvm${LLVM_RELEASE}'
# e.g. "${STAGING_BINDIR}/llvm2.5" or "${STAGING_INCDIR}/llvm2.5"
#
# For your program or library that makes use of llvm you do should not need to
# modify anything as long as it uses the results of various llvm-config
# invocations. If you need customizations something is wrong and it needs to be
# fixed (report bug).
#
# However the *recipe* for your program/library *must* declare
# export WANT_LLVM_RELEASE = "<valid version number>"
# The version number is picked up by a generic wrapper script which just calls
# the variant of the specified version.

DEPENDS = "libffi libxml2-native llvm-native zlib"

inherit perlnative pythonnative cmake


EXTRA_OECMAKE="-DLLVM_ENABLE_RTTI=True \
               -DLLVM_ENABLE_FFI=False \
               -DCMAKE_SYSTEM_NAME=Linux \
	      "

EXTRA_OECMAKE_append_class-native = "\
               -DLLVM_TARGETS_TO_BUILD=host \
               -DCMAKE_BUILD_TYPE=Release \
               -DCMAKE_INSTALL_PREFIX=${prefix} \
              "
EXTRA_OECMAKE_append_class-target = "\
               -DCMAKE_CROSSCOMPILING=True \
"
EXTRA_OEMAKE += "REQUIRES_RTTI=1 VERBOSE=1"

do_configure() {
	# Remove RPATHs
	sed -i 's:$(RPATH) -Wl,$(\(ToolDir\|LibDir\|ExmplDir\))::g' ${S}/Makefile.rules
	# Drop "svn" suffix from version string
	sed -i 's/${PV}svn/${PV}/g' ${S}/configure

	# Fix paths in llvm-config
	sed -i "s|sys::path::parent_path(CurrentPath))\.str()|sys::path::parent_path(sys::path::parent_path(CurrentPath))).str()|g" ${S}/tools/llvm-config/llvm-config.cpp
	sed -ri "s#/(bin|include|lib)(/?\")#/\1/${LLVM_DIR}\2#g" ${S}/tools/llvm-config/llvm-config.cpp
	cmake ${S}
}

do_compile_class-native () {
	oe_runmake
}

do_install_class-native () {
	oe_runmake install DESTDIR=${D}
}
