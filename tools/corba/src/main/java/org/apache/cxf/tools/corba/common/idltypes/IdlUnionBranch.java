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
name|List
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|IdlUnionBranch
extends|extends
name|IdlField
block|{
specifier|private
name|boolean
name|isDefault
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|cases
decl_stmt|;
specifier|private
name|IdlUnionBranch
parameter_list|(
name|IdlUnion
name|union
parameter_list|,
name|String
name|name
parameter_list|,
name|IdlType
name|type
parameter_list|,
name|boolean
name|hasDefault
parameter_list|)
block|{
name|super
argument_list|(
name|union
argument_list|,
name|name
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|this
operator|.
name|isDefault
operator|=
name|hasDefault
expr_stmt|;
name|cases
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
block|}
specifier|public
specifier|static
name|IdlUnionBranch
name|create
parameter_list|(
name|IdlUnion
name|union
parameter_list|,
name|String
name|name
parameter_list|,
name|IdlType
name|type
parameter_list|,
name|boolean
name|isDefault
parameter_list|)
block|{
return|return
operator|new
name|IdlUnionBranch
argument_list|(
name|union
argument_list|,
name|name
argument_list|,
name|type
argument_list|,
name|isDefault
argument_list|)
return|;
block|}
specifier|public
name|void
name|addCase
parameter_list|(
name|String
name|label
parameter_list|)
block|{
if|if
condition|(
operator|!
name|isDefault
condition|)
block|{
name|cases
operator|.
name|add
argument_list|(
name|label
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
if|if
condition|(
name|isDefault
condition|)
block|{
name|pw
operator|.
name|println
argument_list|(
name|indent
argument_list|()
operator|+
literal|"default:"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
for|for
control|(
name|String
name|s
range|:
name|cases
control|)
block|{
name|pw
operator|.
name|println
argument_list|(
name|indent
argument_list|()
operator|+
literal|"case "
operator|+
name|s
operator|+
literal|":"
argument_list|)
expr_stmt|;
block|}
block|}
name|indentMore
argument_list|()
expr_stmt|;
name|super
operator|.
name|write
argument_list|(
name|pw
argument_list|)
expr_stmt|;
name|indentLess
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

