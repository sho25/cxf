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
name|sts
operator|.
name|token
operator|.
name|realm
package|;
end_package

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_class
specifier|public
class|class
name|RelationshipResolver
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|Relationship
argument_list|>
name|relationshipMap
decl_stmt|;
specifier|public
name|RelationshipResolver
parameter_list|(
name|List
argument_list|<
name|Relationship
argument_list|>
name|relationships
parameter_list|)
block|{
name|relationshipMap
operator|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|Relationship
argument_list|>
argument_list|()
expr_stmt|;
for|for
control|(
name|Relationship
name|rel
range|:
name|relationships
control|)
block|{
name|String
name|key
init|=
name|generateKey
argument_list|(
name|rel
operator|.
name|getSourceRealm
argument_list|()
argument_list|,
name|rel
operator|.
name|getTargetRealm
argument_list|()
argument_list|)
decl_stmt|;
name|relationshipMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|rel
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Relationship
name|resolveRelationship
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|String
name|targetRealm
parameter_list|)
block|{
name|String
name|key
init|=
name|generateKey
argument_list|(
name|sourceRealm
argument_list|,
name|targetRealm
argument_list|)
decl_stmt|;
return|return
name|relationshipMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|private
name|String
name|generateKey
parameter_list|(
name|String
name|sourceRealm
parameter_list|,
name|String
name|targetRealm
parameter_list|)
block|{
return|return
operator|new
name|StringBuilder
argument_list|()
operator|.
name|append
argument_list|(
name|sourceRealm
argument_list|)
operator|.
name|append
argument_list|(
literal|">"
argument_list|)
operator|.
name|append
argument_list|(
name|targetRealm
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

