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
name|interceptor
operator|.
name|security
operator|.
name|test
package|;
end_package

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|Principal
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|acl
operator|.
name|Group
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Enumeration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|security
operator|.
name|SimplePrincipal
import|;
end_import

begin_class
specifier|public
class|class
name|GroupWrapper
block|{
specifier|private
name|Principal
name|group
decl_stmt|;
specifier|public
name|GroupWrapper
parameter_list|(
name|String
name|groupName
parameter_list|,
name|String
name|userName
parameter_list|)
block|{
name|SimpleGroup
name|simpeG
init|=
operator|new
name|SimpleGroup
argument_list|(
name|groupName
argument_list|)
decl_stmt|;
name|simpeG
operator|.
name|addMember
argument_list|(
operator|new
name|SimplePrincipal
argument_list|(
name|userName
argument_list|)
argument_list|)
expr_stmt|;
name|group
operator|=
name|simpeG
expr_stmt|;
block|}
specifier|public
name|Principal
name|getGroup
parameter_list|()
block|{
return|return
name|this
operator|.
name|group
return|;
block|}
specifier|private
specifier|static
class|class
name|SimpleGroup
implements|implements
name|Group
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|Set
argument_list|<
name|Principal
argument_list|>
name|principals
decl_stmt|;
name|SimpleGroup
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|principals
operator|=
operator|new
name|HashSet
argument_list|<>
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|addMember
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
return|return
name|this
operator|.
name|principals
operator|.
name|add
argument_list|(
name|principal
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|removeMember
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
return|return
name|this
operator|.
name|principals
operator|.
name|remove
argument_list|(
name|principal
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Enumeration
argument_list|<
name|?
extends|extends
name|Principal
argument_list|>
name|members
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|enumeration
argument_list|(
name|this
operator|.
name|principals
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isMember
parameter_list|(
name|Principal
name|principal
parameter_list|)
block|{
return|return
name|this
operator|.
name|principals
operator|.
name|contains
argument_list|(
name|principal
argument_list|)
return|;
block|}
block|}
block|}
end_class

end_unit

