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
name|systest
operator|.
name|jaxrs
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStream
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
name|io
operator|.
name|AbstractWrappedOutputStream
import|;
end_import

begin_class
specifier|public
class|class
name|ReplacingOutputStream
extends|extends
name|AbstractWrappedOutputStream
block|{
specifier|private
name|char
name|oldChar
decl_stmt|;
specifier|private
name|char
name|newChar
decl_stmt|;
specifier|public
name|ReplacingOutputStream
parameter_list|(
name|OutputStream
name|wrappedStream
parameter_list|,
name|char
name|oldChar
parameter_list|,
name|char
name|newChar
parameter_list|)
block|{
name|super
argument_list|(
name|wrappedStream
argument_list|)
expr_stmt|;
name|this
operator|.
name|oldChar
operator|=
name|oldChar
expr_stmt|;
name|this
operator|.
name|newChar
operator|=
name|newChar
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|write
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|replace
argument_list|(
name|b
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|replace
parameter_list|(
name|byte
index|[]
name|b
parameter_list|)
throws|throws
name|IOException
block|{
name|this
operator|.
name|wrappedStream
operator|.
name|write
argument_list|(
operator|new
name|String
argument_list|(
name|b
argument_list|)
operator|.
name|replace
argument_list|(
name|this
operator|.
name|oldChar
argument_list|,
name|this
operator|.
name|newChar
argument_list|)
operator|.
name|getBytes
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

