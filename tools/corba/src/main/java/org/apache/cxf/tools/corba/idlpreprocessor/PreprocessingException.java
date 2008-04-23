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
name|idlpreprocessor
package|;
end_package

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
import|;
end_import

begin_class
specifier|public
specifier|final
class|class
name|PreprocessingException
extends|extends
name|RuntimeException
block|{
specifier|private
specifier|final
name|int
name|line
decl_stmt|;
specifier|private
specifier|final
name|URL
name|url
decl_stmt|;
specifier|public
name|PreprocessingException
parameter_list|(
name|String
name|message
parameter_list|,
name|URL
name|link
parameter_list|,
name|int
name|lineNo
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
name|this
operator|.
name|url
operator|=
name|link
expr_stmt|;
name|this
operator|.
name|line
operator|=
name|lineNo
expr_stmt|;
block|}
specifier|public
name|PreprocessingException
parameter_list|(
name|String
name|message
parameter_list|,
name|URL
name|link
parameter_list|,
name|int
name|lineNo
parameter_list|)
block|{
name|this
argument_list|(
name|message
argument_list|,
name|link
argument_list|,
name|lineNo
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
name|super
operator|.
name|toString
argument_list|()
operator|+
literal|" at line "
operator|+
name|line
operator|+
literal|" in "
operator|+
name|url
return|;
block|}
specifier|public
name|int
name|getLine
parameter_list|()
block|{
return|return
name|line
return|;
block|}
specifier|public
name|URL
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
block|}
end_class

end_unit

