
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
							placeholder="输入团队名称"
							autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item >
						<el-input v-model="qryForm.activeName"
							placeholder="输入活动名称…"
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
			<div v-if="userType == 1" slot="header">
				<el-button type="primary" size="mini" 
						icon="el-icon-plus" @click="showAddWin()"></el-button>
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="name" label="活动名称"></el-table-column>
					<el-table-column align="center" prop="teamName" label="发布社团"></el-table-column>
					<el-table-column align="center" prop="activeTime" label="活动时间"></el-table-column>
					<el-table-column align="center" prop="total" label="报名人数"></el-table-column>
					<el-table-column align="center" label="参与人员">
						<template slot-scope="scope">
							<el-popover title="活动参与人员" trigger="click" 
								@show="getActivePeople(scope.row.id)" :width="550" placement="left">
								<el-table :data="activeLogs" border>
									<el-table-column align="center" type="index"></el-table-column>
									<el-table-column align="center" prop="userName" label="参与人员"></el-table-column>
									<el-table-column align="center" prop="userPhone" label="联系电话"></el-table-column>
									<el-table-column align="center" prop="createTime" label="报名时间"></el-table-column>
								</el-table>
								<el-button type="text" size="mini" slot="reference">参与人员</el-button>
							</el-popover>
						</template>
					</el-table-column>
					<el-table-column v-if="userType == 0" align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button icon="el-icon-delete" type="danger" size="mini"
								@click="delInfo(scope.row.id)"></el-button>
						</template>
					</el-table-column>
					<el-table-column v-else align="center" label="了解更多">
						<template slot-scope="scope">
							<el-popover trigger="click"  :width="700" placement="left">
								<el-button @click="active(scope.row.id)" style="margin-bottom:15px;" type="primary" size="mini">我要报名</el-button>
								<el-descriptions :column="1" size="small" border>
									<el-descriptions-item>
										<template slot="label">活动标题</template>
										{{ scope.row.name }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">发布社团</template>
										{{ scope.row.teamName }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">活动时间</template>
										{{ scope.row.activeTime }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">活动概述</template>
										{{ scope.row.comm }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">活动要求</template>
										{{ scope.row.ask }}
									</el-descriptions-item>
									<el-descriptions-item>
										<template slot="label">活动详情</template>
										{{ scope.row.detail }}
									</el-descriptions-item>
								</el-descriptions>
								<el-button type="text" size="mini" slot="reference">查看详情</el-button>
							</el-popover>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>

		<el-dialog title="添加信息" :fullscreen="true" :visible.sync="showAddFlag">
			<el-form label-width="90px" :model="activitiesForm">
				<el-form-item label="活动名称">
					<el-input v-model="activitiesForm.name" 
						placeholder="请输入活动名称…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="活动时间">
					<el-date-picker style="width:100%;" value-format="yyyy-MM-dd HH:mm:ss" 
							v-model="activitiesForm.activeTime" 
							type="datetime" placeholder="选择活动时间…"></el-date-picker>
				</el-form-item>
				<el-form-item label="发布社团">
					<el-select style="width:100%;" v-model="activitiesForm.teamId" placeholder="请选择发布社团…">
						<el-option v-for="(item, index) in teams" 
							:key="index" :label="item.name" :value="item.id"></el-option>
					</el-select>
				</el-form-item>
				<el-form-item label="活动概述">
					<el-input type="textarea" rows="6" v-model="activitiesForm.comm" 
						placeholder="请输入活动概述…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="活动要求">
					<el-input type="textarea" rows="6" v-model="activitiesForm.ask" 
						placeholder="请输入活动要求…" autocomplete="off"></el-input>
				</el-form-item>
				<el-form-item label="活动详情">
					<el-input type="textarea" rows="10" v-model="activitiesForm.detail" 
						placeholder="请输入活动详情…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<style>

</style>

<script>
	import {
		addActiveLogs,
		getManTeamList,
		getLoginUser,
		getActiveLogs,
		getPageActivities,
		addActivities,
		delActivities
	} from "../../api";

	export default {

		data() {

			return {
				teams: [],
				userType: "",
				activeLogs: [],
				pageInfos: [],
				pageIndex: 1,
				pageSize: 10,
				pageTotal: 0,
				totalInfo: 0,
				loading: true,
				showAddFlag: false,
				qryForm: {
					token: this.$store.state.token,
					teamName: "",
					activeName: "",
				},
				activitiesForm: {
					id: "",
					name: "",
					comm: "",
					detail: "",
					ask: "",
					total: 1,
					activeTime: "",
					teamId: "",
				}
			}
		},
		methods: {

			getActivePeople(activeId){

				getActiveLogs(activeId).then(resp =>{
					
					this.activeLogs = [];
					this.activeLogs = resp.data;
				});
			},

			getPageInfo(pageIndex, pageSize) {

				getPageActivities(pageIndex, pageSize, this.qryForm.token).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;

					this.loading = false;
				});
			},
			getPageLikeInfo() {

				getPageActivities(1, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.activeName).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {

				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.activeName);
			},
			handleCurrentChange(pageIndex) {

				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.token, this.qryForm.teamName, this.qryForm.activeName);
			},
			initForm() {

				this.activitiesForm = {
					id: "",
					name: "",
					comm: "",
					detail: "",
					ask: "",
					total: 1,
					activeTime: "",
					teamId: "",
				};
			},
			showAddWin() {

				this.initForm();
				this.showAddFlag = true;
			},
			active(id){

				addActiveLogs({token: this.$store.state.token, activeId: id}).then(resp =>{
					
					if(resp.code == 0){

						this.$message({
							message: resp.msg,
							type: 'success'
						});
					}else{

						this.$message({
							message: resp.msg,
							type: 'warning'
						});
					}
				});
			},
			addInfo() {

				addActivities(this.activitiesForm).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize, this.qryForm.token);

					this.showAddFlag = false;

				});
			},
			delInfo(id){
				this.$confirm('删除活动将同时删除报名记录, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					delActivities(id).then(resp => {

						this.$message({
							message: resp.msg,
							type: 'success'
						});

						this.getPageInfo(1, this.pageSize, this.qryForm.token);
					});
				});
			}
		},
		mounted() {

			this.getPageInfo(1, this.pageSize, this.qryForm.token);

			getLoginUser(this.$store.state.token).then(resp =>{

				this.userType = resp.data.type;

				getManTeamList(resp.data.id).then(resp =>{

					this.teams = resp.data;
				});
			});
		}
	}
</script>