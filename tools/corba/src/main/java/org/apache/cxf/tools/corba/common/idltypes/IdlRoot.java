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
name|common
operator|.
name|idltypes
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|PrintWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

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
specifier|final
class|class
name|IdlRoot
extends|extends
name|IdlScopeBase
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|IdlType
argument_list|>
name|primitiveTypes
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includeList
decl_stmt|;
specifier|private
name|IdlRoot
parameter_list|()
block|{
name|super
argument_list|(
literal|null
argument_list|,
literal|""
argument_list|)
expr_stmt|;
name|primitiveTypes
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
name|includeList
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|short
name|i
init|=
name|IdlPrimitive
operator|.
name|MINIMUM
init|;
name|i
operator|<=
name|IdlPrimitive
operator|.
name|MAXIMUM
condition|;
operator|++
name|i
control|)
block|{
name|IdlPrimitive
name|prim
init|=
name|IdlPrimitive
operator|.
name|create
argument_list|(
name|this
argument_list|,
name|i
argument_list|)
decl_stmt|;
name|primitiveTypes
operator|.
name|put
argument_list|(
name|prim
operator|.
name|wsdlName
argument_list|()
argument_list|,
name|prim
argument_list|)
expr_stmt|;
block|}
name|primitiveTypes
operator|.
name|put
argument_list|(
literal|"string"
argument_list|,
name|IdlString
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
name|primitiveTypes
operator|.
name|put
argument_list|(
literal|"wstring"
argument_list|,
name|IdlWString
operator|.
name|create
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|IdlRoot
name|create
parameter_list|()
block|{
return|return
operator|new
name|IdlRoot
argument_list|()
return|;
block|}
specifier|public
name|IdlDefn
name|lookup
parameter_list|(
name|String
name|nm
parameter_list|)
block|{
return|return
name|lookup
argument_list|(
name|nm
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|IdlDefn
name|lookup
parameter_list|(
name|String
name|nm
parameter_list|,
name|boolean
name|undefined
parameter_list|)
block|{
name|IdlDefn
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
operator|!
name|undefined
operator|&&
name|primitiveTypes
operator|.
name|containsKey
argument_list|(
name|nm
argument_list|)
condition|)
block|{
name|result
operator|=
name|primitiveTypes
operator|.
name|get
argument_list|(
name|nm
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|result
operator|=
name|super
operator|.
name|lookup
argument_list|(
name|nm
argument_list|,
name|undefined
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|public
name|void
name|addInclude
parameter_list|(
name|String
name|includefile
parameter_list|)
block|{
if|if
condition|(
operator|!
name|includeList
operator|.
name|contains
argument_list|(
name|includefile
argument_list|)
condition|)
block|{
name|includeList
operator|.
name|add
argument_list|(
name|includefile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
comment|//Write the Include files
for|for
control|(
name|String
name|s
range|:
name|includeList
control|)
block|{
name|pw
operator|.
name|println
argument_list|(
literal|"#include "
operator|+
name|s
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|includeList
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|pw
operator|.
name|println
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|writeFwd
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|super
operator|.
name|write
argument_list|(
name|pw
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

