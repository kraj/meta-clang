[![Yoe Distro CI](https://github.com/kraj/meta-clang/workflows/Yoe%20Distro%20CI/badge.svg?branch=master)](https://github.com/kraj/meta-clang/actions/workflows/yoe.yml?query=workflow%3AYoe+branch%3Amaster)
<table border="0" rules="none">
<tr border="0">
<td width="250" height="150"><img alt="Yocto Project Layer Compatible"
src="images/Yocto_Project™_Badge_Compatible_Web_RGB.png"></td>
</tr>
</table>

# meta-clang (C/C++ frontend and LLVM compiler backend)

This layer provides [clang/llvm](http://clang.llvm.org/) as alternative to system
C/C++ compiler for OpenEmbedded/Yocto Project based distributions. This can cohabit
with GNU compiler and can be used for specific recipes or full system compiler.

# Getting Started

```shell
git clone https://github.com/openembedded/openembedded-core.git
cd openembedded-core
git clone https://github.com/openembedded/bitbake.git
git clone https://github.com/kraj/meta-clang.git

$ . ./oe-init-build-env
```
If using poky ( Yocto project reference Distribution )

```shell
git clone https://git.yoctoproject.org/git/poky
cd poky
git clone https://github.com/kraj/meta-clang.git

$ . ./oe-init-build-env
```

Add meta-clang overlay
```
bitbake-layers add-layer ../meta-clang
```

Check `conf/bblayers.conf` to see that meta-clang is added to layer mix e.g.

# Default Compiler

Note that by default gcc will remain the system compiler, however if you wish
clang to be the default compiler then set

```shell
TOOLCHAIN ?= "clang"
```

in `local.conf`, this would now switch default cross-compiler to be clang
you can select clang per recipe too by writing bbappends for them containing

```shell
TOOLCHAIN = "clang"
```
also look at `conf/nonclangable.conf` for list of recipes which do not yet fully
build with clang.

# Providing LLVM

clang recipes can provide llvm and related packages too, it might be worth using single
provider for llvm and clang to save some compile time and space, select the knobs
to point to clang, default is to use the version provided by core layer.

```shell
PREFERRED_PROVIDER_llvm = "clang"
PREFERRED_PROVIDER_llvm-native = "clang-native"
PREFERRED_PROVIDER_nativesdk-llvm = "nativesdk-clang"
PROVIDES:pn-clang = "llvm"
PROVIDES:pn-clang-native = "llvm-native"
PROVIDES:pn-nativesdk-clang = "nativesdk-llvm"
```
# Default Compiler Runtime

Default is to use GNU runtime `TC_CXX_RUNTIME = "gnu"` which consists of libgcc, libstdc++ to provide C/C++
runtime support. However it's possible to use LLVM runtime to replace it where
compile-rt, llvm libunwind, and libc++ are used to provide C/C++ runtime, while
GNU runtime works with both GCC and Clang, LLVM runtime is only tested with Clang
compiler, switching to use LLVM runtime is done via a config metadata knob

```shell
TC_CXX_RUNTIME = "llvm"
```

TC_CXX_RUNTIME variable influences individual runtime elements and can be set explicitly as well
e.g. `LIBCPLUSPLUS` `COMPILER_RT` and `UNWINDLIB`.

Please note that this will still use crt files from GNU compiler always, while llvm now
do provide crt files, they have not been yet integrated into the toolchain.

# Default C++ Standard Library Switch

Using TC_CXX_RUNTIME variable will select which C++ runtime is used, however it can be overridden
if needed to by modifying `LIBCPLUSPLUS` variable, usually defaults used by `TC_CXX_RUNTIME` are
best fit. e.g. below we select LLVM C++ as default C++ runtime.

```shell
LIBCPLUSPLUS = "-stdlib=libc++"
```

in `local.conf`.
You can select libstdc++ per package too by writing bbappends for them containing

```shell
LIBCPLUSPLUS:toolchain-clang:pn-<recipe> = "-stdlibc=libc++"
```
Defaults are chosen to be GNU for maximum compatibility with existing GNU systems. It's always
good to use single runtime on a system, mixing runtimes can cause complications during
compilation as well as runtime. However, it's up to distribution policies to decide which runtime
to use.

# Adding clang in generated SDK toolchain

Clang based cross compiler is not included into the generated SDK using `bitbake meta-toolchain` or
`bitbake -cpopulate_sdk <image>` if clang is expected to be part of SDK, add `CLANGSDK = "1"`
in `local.conf`

```shell
CLANGSDK = "1"
```

# Kernel build with clang
Newer kernels and Android kernels support clang compilation, and even support LTO, The following takes [rockchip](https://github.com/JeffyCN/meta-rockchip)'s kernel as an example to configure clang compilation. x86, arm and arm64 kernel supported full LLVM toolchain, other arch only support clang. more info https://docs.kernel.org/kbuild/llvm.html

- linux-rockchip_%bbappend
```shell
TOOLCHAIN:forcevariable = "clang"

DEPENDS:append:toolchain-clang = " clang-cross-${TARGET_ARCH}"
KERNEL_CC:toolchain-clang = "${CCACHE}clang ${HOST_CC_KERNEL_ARCH} -fuse-ld=lld ${DEBUG_PREFIX_MAP} -fdebug-prefix-map=${STAGING_KERNEL_DIR}=${KERNEL_SRC_PATH}"
KERNEL_LD:toolchain-clang = "${CCACHE}ld.lld"
KERNEL_AR:toolchain-clang = "${CCACHE}llvm-ar"
```
if you want use LLVM integrated assembler for some older kernel, newer vesion is enabled default.
```shell
do_compile:prepend:toolchain-clang() {
	export LLVM_IAS=1
}

do_compile_kernelmodules:prepend:toolchain-clang() {
	export LLVM_IAS=1
}
```
if you want enable LTO, append follow content.
```
FILESEXTRAPATHS:prepend := "${THISDIR}/${PN}:"
SRC_URI:append:toolchain-clang = "\
	file://lto.cfg \
"
```

`CONFIG_LTO_CLANG`is need for some android based kernel, mainline kernel will do auto detect.

- lto.cfg
```
CONFIG_LTO_CLANG=y
CONFIG_LTO=y
CONFIG_LTO_CLANG_THIN=y
```


# Building

Below we build for qemuarm machine as an example

```shell
$ MACHINE=qemuarm bitbake core-image-full-cmdline
```
# Running

```shell
$ runqemu nographic
```

# Limitations

Few components do not build with clang, if you have a component to add to that list
simply add it to `conf/nonclangable.inc` e.g.

```shell
TOOLCHAIN:pn-<recipe> = "gcc"
```

and OE will start using gcc to cross compile that recipe.

If a component does not build with libc++, you can add it to `conf/nonclangable.inc` e.g.

```shell
CXX:remove:pn-<recipe>:toolchain-clang = " -stdlib=libc++ "
```

# compiler-rt failing in do_configure with custom TARGET_VENDOR

If your DISTRO sets its own value of TARGET_VENDOR, then it needs to be added in
CLANG_EXTRA_OE_VENDORS, it should be done automatically, but if compiler-rt fails
like bellow, then check the end of work-shared/llvm-project-source-12.0.0-r0/temp/log.do_patch
it should have a line like:
NOTE: Adding support following TARGET_VENDOR values: foo in
  /OE/build/oe-core/tmp-glibc/work-shared/llvm-project-source-12.0.0-r0/git/llvm/lib/Support/Triple.cpp and
  /OE/build/oe-core/tmp-glibc/work-shared/llvm-project-source-12.0.0-r0/git/clang/lib/Driver/ToolChains/Gnu.cpp
and check these files if //CLANG_EXTRA_OE_VENDORS* strings were replaced correctly.
Read add_distro_vendor function in recipes-devtools/clang/llvm-project-source.inc for more details.

http://errors.yoctoproject.org/Errors/Details/574365/
```shell
-- Found assembler: TOPDIR/tmp-glibc/work/core2-64-foo-linux/compiler-rt/12.0.0-r0/recipe-sysroot-native/usr/bin/x86_64-foo-linux/x86_64-foo-linux-clang
-- Detecting C compiler ABI info
-- Detecting C compiler ABI info - failed
-- Check for working C compiler: TOPDIR/tmp-glibc/work/core2-64-foo-linux/compiler-rt/12.0.0-r0/recipe-sysroot-native/usr/bin/x86_64-foo-linux/x86_64-foo-linux-clang
-- Check for working C compiler: TOPDIR/tmp-glibc/work/core2-64-foo-linux/compiler-rt/12.0.0-r0/recipe-sysroot-native/usr/bin/x86_64-foo-linux/x86_64-foo-linux-clang - broken
CMake Error at TOPDIR/tmp-glibc/work/core2-64-foo-linux/compiler-rt/12.0.0-r0/recipe-sysroot-native/usr/share/cmake-3.19/Modules/CMakeTestCCompiler.cmake:66 (message):
  The C compiler

    "TOPDIR/tmp-glibc/work/core2-64-foo-linux/compiler-rt/12.0.0-r0/recipe-sysroot-native/usr/bin/x86_64-foo-linux/x86_64-foo-linux-clang"

  is not able to compile a simple test program.

  It fails with the following output:

    Change Dir: TOPDIR/tmp-glibc/work/core2-64-foo-linux/compiler-rt/12.0.0-r0/build/CMakeFiles/CMakeTmp

    Run Build Command(s):ninja cmTC_928f4 && [1/2] Building C object CMakeFiles/cmTC_928f4.dir/testCCompiler.c.o
    [2/2] Linking C executable cmTC_928f4
```

# Dependencies

```shell
URI: https://github.com/openembedded/openembedded-core.git
branch: master
revision: HEAD

URI: ghttps://github.com/openembedded/bitbake.git
branch: master
revision: HEAD
```

# Contributing

You are encouraged to follow Github Pull request workflow
to share changes and following commit message guidelines are recommended: [OE patch guidelines](https://www.openembedded.org/wiki/Commit_Patch_Message_Guidelines).

Layer Maintainer: [Khem Raj](<mailto:raj.khem@gmail.com>)
