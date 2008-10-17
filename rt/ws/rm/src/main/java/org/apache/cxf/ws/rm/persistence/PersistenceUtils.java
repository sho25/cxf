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
name|ws
operator|.
name|rm
operator|.
name|persistence
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBContext
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBElement
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|JAXBException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Marshaller
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|Unmarshaller
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
name|common
operator|.
name|util
operator|.
name|PackageUtils
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
name|helpers
operator|.
name|LoadingByteArrayOutputStream
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
name|ws
operator|.
name|rm
operator|.
name|SequenceAcknowledgement
import|;
end_import

begin_comment
comment|/**  *   */
end_comment

begin_class
specifier|public
specifier|final
class|class
name|PersistenceUtils
block|{
specifier|private
specifier|static
name|PersistenceUtils
name|instance
decl_stmt|;
specifier|private
name|JAXBContext
name|context
decl_stmt|;
specifier|private
name|Unmarshaller
name|unmarshaller
decl_stmt|;
specifier|private
name|Marshaller
name|marshaller
decl_stmt|;
comment|/**      * Prevents instantiation.      */
specifier|private
name|PersistenceUtils
parameter_list|()
block|{     }
specifier|public
specifier|static
name|PersistenceUtils
name|getInstance
parameter_list|()
block|{
if|if
condition|(
literal|null
operator|==
name|instance
condition|)
block|{
name|instance
operator|=
operator|new
name|PersistenceUtils
argument_list|()
expr_stmt|;
block|}
return|return
name|instance
return|;
block|}
specifier|public
name|SequenceAcknowledgement
name|deserialiseAcknowledgment
parameter_list|(
name|InputStream
name|is
parameter_list|)
block|{
name|Object
name|obj
init|=
literal|null
decl_stmt|;
try|try
block|{
name|obj
operator|=
name|getUnmarshaller
argument_list|()
operator|.
name|unmarshal
argument_list|(
name|is
argument_list|)
expr_stmt|;
if|if
condition|(
name|obj
operator|instanceof
name|JAXBElement
argument_list|<
name|?
argument_list|>
condition|)
block|{
name|JAXBElement
argument_list|<
name|?
argument_list|>
name|el
init|=
operator|(
name|JAXBElement
argument_list|<
name|?
argument_list|>
operator|)
name|obj
decl_stmt|;
name|obj
operator|=
name|el
operator|.
name|getValue
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RMStoreException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
operator|(
name|SequenceAcknowledgement
operator|)
name|obj
return|;
block|}
specifier|public
name|InputStream
name|serialiseAcknowledgment
parameter_list|(
name|SequenceAcknowledgement
name|ack
parameter_list|)
block|{
name|LoadingByteArrayOutputStream
name|bos
init|=
operator|new
name|LoadingByteArrayOutputStream
argument_list|()
decl_stmt|;
try|try
block|{
name|getMarshaller
argument_list|()
operator|.
name|marshal
argument_list|(
name|ack
argument_list|,
name|bos
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|JAXBException
name|ex
parameter_list|)
block|{
throw|throw
operator|new
name|RMStoreException
argument_list|(
name|ex
argument_list|)
throw|;
block|}
return|return
name|bos
operator|.
name|createInputStream
argument_list|()
return|;
block|}
specifier|private
name|JAXBContext
name|getContext
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
literal|null
operator|==
name|context
condition|)
block|{
name|context
operator|=
name|JAXBContext
operator|.
name|newInstance
argument_list|(
name|PackageUtils
operator|.
name|getPackageName
argument_list|(
name|SequenceAcknowledgement
operator|.
name|class
argument_list|)
argument_list|,
name|getClass
argument_list|()
operator|.
name|getClassLoader
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|context
return|;
block|}
specifier|private
name|Unmarshaller
name|getUnmarshaller
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
literal|null
operator|==
name|unmarshaller
condition|)
block|{
name|unmarshaller
operator|=
name|getContext
argument_list|()
operator|.
name|createUnmarshaller
argument_list|()
expr_stmt|;
block|}
return|return
name|unmarshaller
return|;
block|}
specifier|private
name|Marshaller
name|getMarshaller
parameter_list|()
throws|throws
name|JAXBException
block|{
if|if
condition|(
literal|null
operator|==
name|marshaller
condition|)
block|{
name|marshaller
operator|=
name|getContext
argument_list|()
operator|.
name|createMarshaller
argument_list|()
expr_stmt|;
block|}
return|return
name|marshaller
return|;
block|}
block|}
end_class

end_unit

