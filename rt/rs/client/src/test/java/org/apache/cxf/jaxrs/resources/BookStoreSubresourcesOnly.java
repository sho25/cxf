begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|jaxrs
operator|.
name|resources
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|PathParam
import|;
end_import

begin_class
annotation|@
name|Path
argument_list|(
literal|"/bookstore/{id}/{id2}/{id3}"
argument_list|)
specifier|public
class|class
name|BookStoreSubresourcesOnly
block|{
annotation|@
name|Path
argument_list|(
literal|"/sub1"
argument_list|)
specifier|public
name|BookStoreSubresourcesOnly
name|getItself
parameter_list|()
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/sub2/{id4}"
argument_list|)
specifier|public
name|BookStoreSubresourcesOnly
name|getItself2
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id4"
argument_list|)
name|String
name|id4
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id1
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id3"
argument_list|)
name|String
name|id3
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/{id4}/sub3"
argument_list|)
specifier|public
name|BookStoreSubresourcesOnly
name|getItself3
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id4"
argument_list|)
name|String
name|id4
parameter_list|)
block|{
return|return
name|this
return|;
block|}
annotation|@
name|Path
argument_list|(
literal|"/sub2/{id4}"
argument_list|)
specifier|public
name|BookStoreSubresourcesOnly
name|getItself4
parameter_list|(
annotation|@
name|PathParam
argument_list|(
literal|"id4"
argument_list|)
name|String
name|id4
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id"
argument_list|)
name|String
name|id1
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id2"
argument_list|)
name|String
name|id2
parameter_list|,
annotation|@
name|PathParam
argument_list|(
literal|"id3"
argument_list|)
name|String
name|id3
parameter_list|)
block|{
return|return
name|this
return|;
block|}
block|}
end_class

end_unit

