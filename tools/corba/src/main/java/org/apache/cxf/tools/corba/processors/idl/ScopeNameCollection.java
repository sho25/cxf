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
name|tools
operator|.
name|corba
operator|.
name|processors
operator|.
name|idl
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
name|Map
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|ScopeNameCollection
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|Scope
argument_list|>
name|scopedNames
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|add
parameter_list|(
name|Scope
name|scope
parameter_list|)
block|{
name|scopedNames
operator|.
name|put
argument_list|(
name|scope
operator|.
name|toString
argument_list|()
argument_list|,
name|scope
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|remove
parameter_list|(
name|Scope
name|scope
parameter_list|)
block|{
name|scopedNames
operator|.
name|remove
argument_list|(
name|scope
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Scope
name|getScope
parameter_list|(
name|Scope
name|scope
parameter_list|)
block|{
return|return
name|scopedNames
operator|.
name|get
argument_list|(
name|scope
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|Scope
name|getScope
parameter_list|(
name|String
name|scopename
parameter_list|)
block|{
return|return
name|scopedNames
operator|.
name|get
argument_list|(
name|scopename
argument_list|)
return|;
block|}
block|}
end_class

end_unit

