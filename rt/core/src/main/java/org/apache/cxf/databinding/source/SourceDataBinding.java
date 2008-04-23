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
name|databinding
operator|.
name|source
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamReader
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|stream
operator|.
name|XMLStreamWriter
import|;
end_import

begin_import
import|import
name|org
operator|.
name|w3c
operator|.
name|dom
operator|.
name|Node
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
name|databinding
operator|.
name|DataReader
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
name|databinding
operator|.
name|DataWriter
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
name|service
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * A simple databinding implementation which reads and writes Source objects.  */
end_comment

begin_class
specifier|public
class|class
name|SourceDataBinding
extends|extends
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|databinding
operator|.
name|AbstractDataBinding
block|{
specifier|private
name|XMLStreamDataReader
name|xsrReader
decl_stmt|;
specifier|private
name|XMLStreamDataWriter
name|xswWriter
decl_stmt|;
specifier|private
name|NodeDataWriter
name|nodeWriter
decl_stmt|;
specifier|private
name|NodeDataReader
name|nodeReader
decl_stmt|;
specifier|public
name|SourceDataBinding
parameter_list|()
block|{
name|super
argument_list|()
expr_stmt|;
name|this
operator|.
name|xsrReader
operator|=
operator|new
name|XMLStreamDataReader
argument_list|()
expr_stmt|;
name|this
operator|.
name|xswWriter
operator|=
operator|new
name|XMLStreamDataWriter
argument_list|()
expr_stmt|;
name|this
operator|.
name|nodeReader
operator|=
operator|new
name|NodeDataReader
argument_list|()
expr_stmt|;
name|this
operator|.
name|nodeWriter
operator|=
operator|new
name|NodeDataWriter
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|(
name|Service
name|service
parameter_list|)
block|{
comment|// do nothing
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|DataReader
argument_list|<
name|T
argument_list|>
name|createReader
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
name|XMLStreamReader
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataReader
argument_list|<
name|T
argument_list|>
operator|)
name|xsrReader
return|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Node
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataReader
argument_list|<
name|T
argument_list|>
operator|)
name|nodeReader
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The type "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" is not supported."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedReaderFormats
parameter_list|()
block|{
return|return
operator|new
name|Class
index|[]
block|{
name|XMLStreamReader
operator|.
name|class
block|,
name|Node
operator|.
name|class
block|}
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
parameter_list|<
name|T
parameter_list|>
name|DataWriter
argument_list|<
name|T
argument_list|>
name|createWriter
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|cls
parameter_list|)
block|{
if|if
condition|(
name|cls
operator|==
name|XMLStreamWriter
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
name|xswWriter
return|;
block|}
elseif|else
if|if
condition|(
name|cls
operator|==
name|Node
operator|.
name|class
condition|)
block|{
return|return
operator|(
name|DataWriter
argument_list|<
name|T
argument_list|>
operator|)
name|nodeWriter
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"The type "
operator|+
name|cls
operator|.
name|getName
argument_list|()
operator|+
literal|" is not supported."
argument_list|)
throw|;
block|}
block|}
specifier|public
name|Class
argument_list|<
name|?
argument_list|>
index|[]
name|getSupportedWriterFormats
parameter_list|()
block|{
return|return
operator|new
name|Class
index|[]
block|{
name|XMLStreamWriter
operator|.
name|class
block|,
name|Node
operator|.
name|class
block|}
return|;
block|}
block|}
end_class

end_unit

