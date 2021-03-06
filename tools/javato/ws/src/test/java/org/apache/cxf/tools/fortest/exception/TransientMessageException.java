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
name|fortest
operator|.
name|exception
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlTransient
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|TransientMessageException
extends|extends
name|Exception
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
name|int
name|idCode
decl_stmt|;
specifier|public
name|TransientMessageException
parameter_list|()
block|{     }
specifier|public
name|TransientMessageException
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|idCode
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|TransientMessageException
parameter_list|(
name|int
name|i
parameter_list|,
name|String
name|message
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|)
expr_stmt|;
name|idCode
operator|=
name|i
expr_stmt|;
block|}
specifier|public
name|TransientMessageException
parameter_list|(
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|cause
argument_list|)
expr_stmt|;
block|}
specifier|public
name|TransientMessageException
parameter_list|(
name|String
name|message
parameter_list|,
name|Throwable
name|cause
parameter_list|)
block|{
name|super
argument_list|(
name|message
argument_list|,
name|cause
argument_list|)
expr_stmt|;
block|}
annotation|@
name|XmlTransient
specifier|public
name|String
name|getMessage
parameter_list|()
block|{
return|return
name|super
operator|.
name|getMessage
argument_list|()
return|;
block|}
specifier|public
name|int
name|getIDCode
parameter_list|()
block|{
return|return
name|idCode
return|;
block|}
specifier|public
name|void
name|setIDCode
parameter_list|(
name|int
name|i
parameter_list|)
block|{
name|idCode
operator|=
name|i
expr_stmt|;
block|}
block|}
end_class

end_unit

