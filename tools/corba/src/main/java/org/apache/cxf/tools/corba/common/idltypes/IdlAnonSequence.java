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

begin_class
specifier|public
specifier|final
class|class
name|IdlAnonSequence
extends|extends
name|IdlSequenceBase
block|{
specifier|private
name|IdlAnonSequence
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|IdlType
name|elem
parameter_list|,
name|int
name|bound
parameter_list|)
block|{
name|super
argument_list|(
name|parent
argument_list|,
literal|""
argument_list|,
name|elem
argument_list|,
name|bound
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|IdlAnonSequence
name|create
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|IdlType
name|elem
parameter_list|)
block|{
return|return
operator|new
name|IdlAnonSequence
argument_list|(
name|parent
argument_list|,
name|elem
argument_list|,
literal|0
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|IdlAnonSequence
name|create
parameter_list|(
name|IdlScopeBase
name|parent
parameter_list|,
name|IdlType
name|elem
parameter_list|,
name|int
name|bound
parameter_list|)
block|{
return|return
operator|new
name|IdlAnonSequence
argument_list|(
name|parent
argument_list|,
name|elem
argument_list|,
name|bound
argument_list|)
return|;
block|}
specifier|public
name|String
name|fullName
parameter_list|()
block|{
return|return
name|seqName
argument_list|(
name|elemType
argument_list|()
operator|.
name|fullName
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|String
name|fullName
parameter_list|(
name|IdlScopedName
name|rel
parameter_list|)
block|{
return|return
name|seqName
argument_list|(
name|elemType
argument_list|()
operator|.
name|fullName
argument_list|(
name|rel
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|write
parameter_list|(
name|PrintWriter
name|pw
parameter_list|)
block|{
comment|// intentionally empty
block|}
specifier|private
name|String
name|seqName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|StringBuilder
name|str
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|str
operator|.
name|append
argument_list|(
literal|"sequence<"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|int
name|bnd
init|=
name|bound
argument_list|()
decl_stmt|;
if|if
condition|(
name|bnd
operator|!=
literal|0
condition|)
block|{
name|str
operator|.
name|append
argument_list|(
literal|", "
argument_list|)
operator|.
name|append
argument_list|(
name|bnd
argument_list|)
expr_stmt|;
block|}
name|str
operator|.
name|append
argument_list|(
literal|"> "
argument_list|)
expr_stmt|;
return|return
name|str
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

