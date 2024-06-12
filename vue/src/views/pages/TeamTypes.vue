<template>
	<div class="fater-body-show">
		<el-card shadow="never">
			<div slot="header">
				信息查询
			</div>
			<div>
				<el-form :inline="true" :model="qryForm">
					<el-form-item>
						<el-input v-model="qryForm.name" placeholder="输入社团类型名称…" autocomplete="off"></el-input>
					</el-form-item>
					<el-form-item>
						<el-button type="primary" icon="el-icon-search" @click="getPageLikeInfo()"></el-button>
					</el-form-item>
				</el-form>
			</div>
		</el-card>

		<el-card shadow="never">
			<div slot="header">
				<el-button type="primary" size="mini" icon="el-icon-plus" @click="showAddWin()"></el-button>
			</div>
			<div>
				<el-table v-loading="loading" element-loading-text="拼命加载中" element-loading-spinner="el-icon-loading"
					element-loading-background="rgba(124, 124, 124, 0.8)" :data="pageInfos" border>
					<el-table-column align="center" type="index"></el-table-column>
					<el-table-column align="center" prop="name" label="类型名称"></el-table-column>
					<el-table-column align="center" prop="createTime" label="创建时间"></el-table-column>
					<el-table-column align="center" label="操作处理">
						<template slot-scope="scope">
							<el-button icon="el-icon-edit" type="primary" size="mini" @click="showUpdWin(scope.row)">
							</el-button>
							<el-button icon="el-icon-delete" type="danger" size="mini" @click="delInfo(scope.row.id)">
							</el-button>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination v-if="pageTotal > 1" style="margin-top: 15px;" @size-change="handleSizeChange"
					@current-change="handleCurrentChange" :current-page="pageIndex" :page-sizes="[5, 10, 20, 50]"
					:page-size="pageSize" layout="total, sizes, prev, pager, next, jumper" :total="totalInfo">
				</el-pagination>
			</div>
		</el-card>

		<el-dialog title="添加信息" width="600px" :visible.sync="showAddFlag">
			<el-form label-width="90px" :model="teamTypesForm">
				<el-form-item label="类型名称">
					<el-input v-model="teamTypesForm.name" placeholder="请输入类型名称…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showAddFlag = false">取 消</el-button>
				<el-button type="primary" @click="addInfo()">确 定</el-button>
			</div>
		</el-dialog>

		<el-dialog title="修改信息" width="600px" :visible.sync="showUpdFlag">
			<el-form label-width="90px" :model="teamTypesForm">
				<el-form-item label="类型名称">
					<el-input v-model="teamTypesForm.name" placeholder="请输入类型名称…" autocomplete="off"></el-input>
				</el-form-item>
			</el-form>
			<div slot="footer" class="dialog-footer">
				<el-button @click="showUpdFlag = false">取 消</el-button>
				<el-button type="primary" @click="updInfo()">确 定</el-button>
			</div>
		</el-dialog>
	</div>
</template>

<style>

</style>

<script>
	import {
		getPageTeamTypes,
		addTeamTypes,
		updTeamTypes,
		delTeamTypes
	} from "../../api";

	export default {

		data() {

			return {
				pageInfos: [],
				pageIndex: 1,
				pageSize: 10,
				pageTotal: 0,
				totalInfo: 0,
				loading: true,
				showAddFlag: false,
				showUpdFlag: false,
				qryForm: {
					name: "",
				},
				teamTypesForm: {
					id: "",
					name: "",
				}
			}
		},
		methods: {

			getPageInfo(pageIndex, pageSize) {

				getPageTeamTypes(pageIndex, pageSize).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.pageTotal = resp.data.pageTotal;
					this.totalInfo = resp.data.count;

					this.loading = false;
				});
			},
			getPageLikeInfo() {

				getPageTeamTypes(1, this.pageSize, this.qryForm.name).then(resp => {

					this.pageInfos = resp.data.data;
					this.pageIndex = resp.data.pageIndex;
					this.pageSize = resp.data.pageSize;
					this.totalInfo = resp.data.count;
					this.pageTotal = resp.data.pageTotal;
					this.loading = false;
				});
			},
			handleSizeChange(pageSize) {

				this.getPageInfo(this.pageIndex, pageSize, this.qryForm.name);
			},
			handleCurrentChange(pageIndex) {

				this.getPageInfo(pageIndex, this.pageSize, this.qryForm.name);
			},
			initForm() {

				this.teamTypesForm = {
					id: "",
					name: "",
				};
			},
			showAddWin() {

				this.showAddFlag = true;
			},
			showUpdWin(row) {

				this.teamTypesForm = row;
				this.showUpdFlag = true;
			},
			addInfo() {

				addTeamTypes(this.teamTypesForm).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize);

					this.showAddFlag = false;

					this.initForm();
				});
			},
			updInfo() {

				updTeamTypes(this.teamTypesForm).then(resp => {

					this.$message({
						message: resp.msg,
						type: 'success'
					});

					this.getPageInfo(1, this.pageSize);

					this.showUpdFlag = false;

					this.initForm();
				});
			},
			delInfo(id) {
				this.$confirm('即将删除相关信息, 是否继续?', '提示', {
					confirmButtonText: '确定',
					cancelButtonText: '取消',
					type: 'warning'
				}).then(() => {

					delTeamTypes(id).then(resp => {
						
						if(resp.code == 0){

							this.$message({
								message: resp.msg,
								type: 'success'
							});

							this.getPageInfo(1, this.pageSize);
						}else{

							this.$message({
								message: resp.msg,
								type: 'warning'
							});
						}
					});
				});
			}
		},
		mounted() {

			this.getPageInfo(1, this.pageSize);
		}
	}
</script>