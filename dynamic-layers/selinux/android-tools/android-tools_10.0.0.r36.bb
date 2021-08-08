DESCRIPTION = "Different utilities from Android"
SECTION = "console/utils"
LICENSE = "Apache-2.0 & GPL-2.0 & BSD-2-Clause & BSD-3-Clause"
LIC_FILES_CHKSUM = " \
    file://${COMMON_LICENSE_DIR}/Apache-2.0;md5=89aea4e17d99a7cacdbeed46a0096b10 \
    file://${COMMON_LICENSE_DIR}/GPL-2.0-only;md5=801f80980d171dd6425610833a22dbe6 \
    file://${COMMON_LICENSE_DIR}/BSD-2-Clause;md5=cb641bc04cda31daea161b1bc15da69f \
    file://${COMMON_LICENSE_DIR}/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
"
DEPENDS = "libbsd libpcre zlib libcap libusb squashfs-tools p7zip libselinux"

ANDROID_MIRROR = "android.googlesource.com"

# matches with 10.0.0+r36
SRCREV_boringssl = "ae2dd49c7cb74d04bdba7c1c9bd62c1e9cdf98f6"
SRCREV_core = "5aa13b053182b758d7a19db0c83e1b9b5bf1ec2e"
SRCREV_extras = "d31740f9d0399f8b938e88e58843d966e1cccab6"
SRCREV_libhardware = "c6925520342a7d37758f85eb1cf3baa20a7b7a18"
SRCREV_build = "28768b3120f751583a2743101b892f210d4715cf"
SRCREV_libunwind = "03a963ecf6ea836b38b3537cbcda0ecfd7a77393"

SRC_URI = " \
    git://salsa.debian.org/android-tools-team/android-platform-external-boringssl;name=boringssl;protocol=https;nobranch=1;destsuffix=git/external/boringssl \
    git://salsa.debian.org/android-tools-team/android-platform-system-core;name=core;protocol=https;nobranch=1;destsuffix=git/system/core \
    git://salsa.debian.org/android-tools-team/android-platform-system-extras;name=extras;protocol=https;nobranch=1;destsuffix=git/system/extras \
    git://${ANDROID_MIRROR}/platform/hardware/libhardware;name=libhardware;protocol=https;nobranch=1;destsuffix=git/hardware/libhardware \
    git://salsa.debian.org/android-tools-team/android-platform-build.git;name=build;protocol=https;nobranch=1;destsuffix=git/build \
    git://salsa.debian.org/android-tools-team/android-platform-external-libunwind.git;protocol=https;name=libunwind;nobranch=1;destsuffix=git/external/libunwind \
    file://adb_mk_change_out_dir.patch \
    file://libadb_mk_change_out_dir.patch \
    file://fastboot_compile_remove_gtest.patch \
    file://fastboot_mk_change_out_dir.patch \
    file://fastboot_dont_use_sparse_file_import_auto_in_load_buf_fd.patch \
    file://libbase_mk_change_out_dir.patch \
    file://libext4_utils_mk_change_out_dir.patch \
    file://libcrypto_mk_change_out_dir.patch \
    file://libcrypto_utils_mk_change_out_dir.patch \
    file://libcutils_mk_change_out_dir.patch \
    file://libfec_mk_change_out_dir.patch \
    file://img2simg_change_out_dir.patch \
    file://simg2img_change_out_dir.patch \
    file://liblog_mk_change_out_dir.patch \
    file://libsparse_mk_change_out_dir.patch \
    file://libziparchive_mk_change_out_dir.patch \
    file://libbacktrace_mk_change_out_dir.patch \
    file://libunwind_mk_change_out_dir.patch \
    file://use_name_space_std_to_compile_libbacktrace.patch \
    file://rules_yocto.mk;subdir=git \
    file://android-tools-adbd.service \
"

S = "${WORKDIR}/git"
B = "${WORKDIR}/${BPN}"

#apply all the patches maintained in the debian version.
do_unpack_and_patch_debian() {
    cd ${S}/system/core
    for i in `find ${S}/system/core/debian/patches -name "*.patch"`; do
        patch -p1 < $i
    done
    #a patch with no .patch extention, lets apply that
    patch -p1 < ${S}/system/core/debian/patches/Added-missing-headers
    cd ${S}/external/libunwind
    for i in `find ${S}/external/libunwind/debian/patches -name "*.patch"`; do
        patch -p1 < $i
    done
}
addtask unpack_and_patch_debian after do_unpack before do_patch

# http://errors.yoctoproject.org/Errors/Details/1debian881/
ARM_INSTRUCTION_SET_armv4 = "arm"
ARM_INSTRUCTION_SET_armv5 = "arm"

COMPATIBLE_HOST_powerpc = "(null)"
COMPATIBLE_HOST_powerpc64 = "(null)"
COMPATIBLE_HOST_powerpc64le = "(null)"

inherit systemd clang

TOOLCHAIN = "clang"
DEPENDS += "\
    clang-cross-${TARGET_ARCH} \
"

SYSTEMD_SERVICE_${PN} = "android-tools-adbd.service"

# Find libbsd headers during native builds
CC:append_class-native = " -I${STAGING_INCDIR}"
CC:append_class-nativesdk = " -I${STAGING_INCDIR}"

PREREQUISITE_core = "libbase libsparse liblog libcutils"
TOOLS_TO_BUILD = "libcrypto_utils libadb libziparchive fastboot adb img2simg simg2img libbacktrace"

# Adb needs sys/capability.h, which is not available for native*
TOOLS_class-native = "boringssl fastboot ext4_utils mkbootimg"
TOOLS_class-nativesdk = "boringssl fastboot ext4_utils mkbootimg"

do_compile() {

    case "${HOST_ARCH}" in
      arm)
        export android_arch=linux-arm
        cpu=arm
        deb_host_arch=arm
      ;;
      aarch64)
        export android_arch=linux-arm64
        cpu=arm64
        deb_host_arch=arm64
      ;;
      riscv64)
        export android_arch=linux-riscv64
      ;;
      mips|mipsel)
        export android_arch=linux-mips
        cpu=mips
        deb_host_arch=mips
      ;;
      mips64|mips64el)
        export android_arch=linux-mips64
        cpu=mips64
        deb_host_arch=mips64
      ;;
      powerpc|powerpc64)
        export android_arch=linux-ppc
      ;;
      i586|i686|x86_64)
        export android_arch=linux-x86
        cpu=x86_64
        deb_host_arch=amd64
      ;;
    esac

    export SRCDIR=${S}

    oe_runmake -f ${S}/external/boringssl/debian/libcrypto.mk -C ${S}/external/boringssl
    oe_runmake -f ${S}/external/libunwind/debian/libunwind.mk -C ${S}/external/libunwind CPU=${cpu}

    for tool in ${PREREQUISITE_core}; do
      oe_runmake -f ${S}/system/core/debian/${tool}.mk -C ${S}/system/core
    done

    for i in `find ${S}/system/extras/debian/ -name "*.mk"`; do
        oe_runmake -f $i -C ${S}/system/extras
    done

    for tool in ${TOOLS_TO_BUILD}; do
        if tool == "libbacktrace"; then
            oe_runmake -f ${S}/system/core/debian/${tool}.mk -C ${S}/system/core DEB_HOST_ARCH=${deb_host_arch}
        else
            oe_runmake -f ${S}/system/core/debian/${tool}.mk -C ${S}/system/core
        fi
    done

}

do_install() {
    if echo ${TOOLS_TO_BUILD} | grep -q "ext4_utils" ; then
        install -D -p -m0755 ${S}/system/core/libsparse/simg_dump.py ${D}${bindir}/simg_dump
    fi

    if echo ${TOOLS_TO_BUILD} | grep -q "adb " ; then
        install -d ${D}${bindir}
        install -m0755 ${S}/debian/out/usr/lib/android/adb/adb ${D}${bindir}
    fi

    if echo ${TOOLS_TO_BUILD} | grep -q "adbd" ; then
        install -d ${D}${bindir}
        install -m0755 ${B}/adbd/adbd ${D}${bindir}
    fi

    # Outside the if statement to avoid errors during do_package
    install -D -p -m0644 ${WORKDIR}/android-tools-adbd.service \
      ${D}${systemd_unitdir}/system/android-tools-adbd.service

    if echo ${TOOLS_TO_BUILD} | grep -q "fastboot" ; then
        install -d ${D}${bindir}
        install -m0755 ${S}/debian/out/usr/bin/fastboot/fastboot ${D}${bindir}
    fi

    install -d  ${D}${libdir}/android/
    install -m0755 ${S}/debian/out/usr/lib/android/*.so.* ${D}${libdir}/android/
    if echo ${TOOLS_TO_BUILD} | grep -q "mkbootimg" ; then
        install -d ${D}${bindir}
        install -m0755 ${B}/mkbootimg/mkbootimg ${D}${bindir}
    fi
}

PACKAGES += "${PN}-fstools"

RDEPENDS_${BPN} = "${BPN}-conf p7zip"

FILES_${PN}-fstools = "\
    ${bindir}/ext2simg \
    ${bindir}/ext4fixup \
    ${bindir}/img2simg \
    ${bindir}/make_ext4fs \
    ${bindir}/simg2img \
    ${bindir}/simg2simg \
    ${bindir}/simg_dump \
    ${bindir}/mkuserimg \
"
FILES:${PN} += "${libdir}/android ${libdir}/android/*"

BBCLASSEXTEND = "native"
