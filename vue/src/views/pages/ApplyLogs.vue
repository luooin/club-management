<template>
	<div class="fater-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item >
						<el-input v-model="qryForm.teamName"
							placeholder="输入团队名称…"
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item >
						<el-input v-model="qryForm.userName"
							placeholder="输入成员姓名…"
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" 
							icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
					</el-form-item>
				</el-form>
			</div>
		</el-card>

		<el-card shadow="never">
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="teamName" label="社团名称"></el-table-column>
					<el-table-column align="center" prop="userName" label="申请人姓名"></el-table-column>
					<el-table-column align="center" prop="userGender" label="申请人性别"></el-table-column>
					<el-table-column align="center" prop="userPhone" label="申请人电话"></el-table-column>
					<el-table-column align="center" prop="createTime" label="申请时间"></el-table-column>
					<el-table-column v-if="userType == 1" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button v-if="scope.row.status == 0" 
								type="primary" size="mini" @click="updInfo(scope.row, 1)">通过</el-button>
							<el-button v-if="scope.row.status == 0" 
								type="danger" size="mini" @click="updInfo(scope.row, 2)">驳回</el-button>
							<el-tag effect="plain" type="success" v-if="scope.row.status == 1">已通过</el-tag>
							<el-tag effect="plain" type="danger" v-if="scope.row.status == 2">已驳回</el-tag>
						</template>
					</el-table-column>
					<el-table-column v-else align="center" label="申请状态">
						<template slot-scope="scope">
							<el-tag effect="plain" type="warning" v-if="scope.row.status == 0">审核中</el-tag>
							<el-tag effect="plain" type="success" v-if="scope.row.status == 1">已通过</el-tag>
							<el-tag effect="plain" type="danger" v-if="scope.row.status == 2">已驳回</el-tag>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>
	</div>
</template>

<style>

</style>

<script>
	import {
		getLoginUser,
		getPageApplyLogs,
		updApplyLogs
	} from "../../api";

	export default {

		data() {

			return {
				userType: "",
				pageInfos: [],
				pageIndex: 1,
				pageSize: 10,
				pageTotal: 0,
				totalInfo: 0,
				loading: true,
				qryForm: {
					token: this.$store.state.token,
					teamName: "",
					userName: "",
				}
			}
		},
		methods: {

			getPageInfo(pageIndex, pageSize) {

				getPageApplyLogs(pageIndex, pageSize, this.qryForm.token).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;

					this.loading = false;
				});
			},
			getPageLikeInfo() {

				getPageApplyLogs(1, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {

				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName);
			},
			handleCurrentChange(pageIndex) {

				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.userName);
			},
			updInfo(data, status) {

				data['status'] = status;
				updApplyLogs(data).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize);

					this.initForm();
				});
			}
		},
		mounted() {

			this.getPageInfo(1, this.pageSize, this.qryForm.token);

			getLoginUser(this.$store.state.token).then(resp =>{

				this.userType = resp.data.type;
			});
		}
	}
</script>