<template>
    <div class="fater-body-show">
        <el-row :gutter="15">
            <el-col :span="16">
                <div class="fater-welcome-panel"></div>
            </el-col>
            <el-col :span="8">
                <el-card shadow="never">
                    <div>
                        <el-descriptions title="个人资料" :column="1" size="small" border>
                            <el-descriptions-item>
                                <template slot="label">
                                    用户ID
                                </template>
                                {{ loginUser.id }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    用户姓名
                                </template>
                                {{ loginUser.name }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    用户性别
                                </template>
                                {{ loginUser.gender }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    用户年龄
                                </template>
                                {{ loginUser.age }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    联系电话
                                </template>
                                {{ loginUser.phone }}
                            </el-descriptions-item>
                            <el-descriptions-item>
                                <template slot="label">
                                    联系地址
                                </template>
                                {{ loginUser.address }}
                            </el-descriptions-item>
                        </el-descriptions>
                    </div>
                </el-card>
            </el-col>
        </el-row>
        <el-row :gutter="15">
            <el-col :span="16">
                <el-card shadow="never">
                    <div slot="header">系统通知</div>
                    <div>
                        <el-timeline>
                            <el-timeline-item color="#E6A23C" v-for="(item, index) in sysNotices" :key="index"
                                :timestamp="item.createTime" placement="top">
                                <el-card>
                                    <h4 style="font-size: 16px; line-height:28px;margin-bottom:15px;">{{ item.title }}</h4>
                                    <p style="font-size: 14px; line-height:28px;">{{ item.detail }}</p>
                                </el-card>
                            </el-timeline-item>
                        </el-timeline>
                    </div>
                </el-card>
            </el-col>
            <el-col :span="8">
                <el-card shadow="never">
                    <div slot="header">系统信息</div>
                    <div>
                        <el-card>
                            <h4 style="font-size: 16px; line-height:28px;margin-bottom:15px;">测试系统信息</h4>
                            <p style="font-size: 14px; line-height:28px;">
                                测试信息！测试信息！测试信息！测试信息！
                            </p>
                        </el-card>
                    </div>
                </el-card>
            </el-col>
        </el-row>
    </div>
</template>

<style></style>

<script>

import {
    getLoginUser,
    getSysNoticeList
} from "../../api";

export default {
    data() {

        return {
            loginUser: {},
            sysNotices: [],
        }
    },
    mounted() {

        getLoginUser(this.$store.state.token).then(resp => {

            this.loginUser = resp.data;
        });

        getSysNoticeList(this.$store.state.token).then(resp => {

            this.sysNotices = resp.data;
        });
    }
}
</script>