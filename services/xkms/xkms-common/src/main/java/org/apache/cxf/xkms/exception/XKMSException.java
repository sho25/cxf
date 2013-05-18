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
name|xkms
operator|.
name|exception
package|;
end_package

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMajorEnum
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
name|xkms
operator|.
name|model
operator|.
name|xkms
operator|.
name|ResultMinorEnum
import|;
end_import

begin_class
specifier|public
class|class
name|XKMSException
extends|extends
name|RuntimeException
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7247415453067157299L
decl_stmt|;
specifier|private
name|ResultMajorEnum
name|resultMajor
decl_stmt|;
specifier|private
name|ResultMinorEnum
name|resultMinor
decl_stmt|;
specifier|public
name|XKMSException
parameter_list|(
name|ResultMajorEnum
name|resultMajor
parameter_list|,
name|ResultMinorEnum
name|resultMinor
parameter_list|)
block|{
name|super
argument_list|(
name|String
operator|.
name|format
argument_list|(
literal|"Result major: %s; result minor: %s"
argument_list|,
name|resultMajor
operator|.
name|toString
argument_list|()
argument_list|,
name|resultMinor
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|resultMajor
operator|=
name|resultMajor
expr_stmt|;
name|this
operator|.
name|resultMinor
operator|=
name|resultMinor
expr_stmt|;
block|}
specifier|public
name|XKMSException
parameter_list|(
name|ResultMajorEnum
name|resultMajor
parameter_list|,
name|ResultMinorEnum
name|resultMinor
parameter_list|,
name|String
name|msg
parameter_list|)
block|{
name|super
argument_list|(
operator|(
name|msg
operator|!=
literal|null
operator|)
condition|?
name|msg
else|:
name|String
operator|.
name|format
argument_list|(
literal|"Result major: %s; result minor: %s"
argument_list|,
name|resultMajor
operator|.
name|toString
argument_list|()
argument_list|,
name|resultMinor
operator|.
name|toString
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|resultMajor
operator|=
name|resultMajor
expr_stmt|;
name|this
operator|.
name|resultMinor
operator|=
name|resultMinor
expr_stmt|;
block|}
specifier|public
name|XKMSException
parameter_list|(
name|ResultMajorEnum
name|resultMajor
parameter_list|,
name|ResultMinorEnum
name|resultMinor
parameter_list|,
name|String
name|arg0
parameter_list|,
name|Throwable
name|arg1
parameter_list|)
block|{
name|super
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|)
expr_stmt|;
name|this
operator|.
name|resultMajor
operator|=
name|resultMajor
expr_stmt|;
name|this
operator|.
name|resultMinor
operator|=
name|resultMinor
expr_stmt|;
block|}
specifier|public
name|XKMSException
parameter_list|(
name|String
name|arg0
parameter_list|)
block|{
name|super
argument_list|(
name|arg0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|XKMSException
parameter_list|(
name|String
name|arg0
parameter_list|,
name|Throwable
name|arg1
parameter_list|)
block|{
name|super
argument_list|(
name|arg0
argument_list|,
name|arg1
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ResultMajorEnum
name|getResultMajor
parameter_list|()
block|{
return|return
name|resultMajor
return|;
block|}
specifier|public
name|ResultMinorEnum
name|getResultMinor
parameter_list|()
block|{
return|return
name|resultMinor
return|;
block|}
block|}
end_class

end_unit

